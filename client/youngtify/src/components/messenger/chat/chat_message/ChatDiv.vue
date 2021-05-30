<template>
  <div
    class="w-full chat-content-bottom bc-cpn-global p-2 flex items-center justify-between relative"
  >
    <v-btn class="mr-1" icon color="#3a8df5" @click="openChooseImg">
      <v-icon size="28">{{ iconImage }}</v-icon>
    </v-btn>
    <input
      ref="file"
      @change="selectFile"
      type="file"
      multiple
      class="hidden"
    />
    <div class="chat-div h-10 relative">
      <div
        v-show="images.length > 0"
        :style="`top:-${top}px`"
        class="p-4 image-list absolute flex"
      >
        <ImageChat
          v-for="(image, index) in imagesBase64"
          :index="index"
          :key="index"
          :image="image"
          @onDeleteImage="onDeleteImage"
        />
        <div class="add-image w-12 h-12 br-global cursor-pointer" v-ripple>
          <v-icon @click="onAddImage" size="48">mdi-image-plus</v-icon>
        </div>
      </div>
      <div
        contenteditable="true"
        data-placeholder="Aa"
        class="absolute input-div w-full cursor-pointer"
        ref="chatInput"
        @input="updateVal($event.target)"
        @keyup="setIsShowIcon"
      ></div>
    </div>
    <v-btn icon class="ml-1" v-show="!isShowIcon" @click="sendMsg">
      <svg width="24" height="24" viewBox="0 0 24 24">
        <path
          d="M16.6915026,12.4744748 L3.50612381,13.2599618 C3.19218622,13.2599618 3.03521743,13.4170592 3.03521743,13.5741566 L1.15159189,20.0151496 C0.8376543,20.8006365 0.99,21.89 1.77946707,22.52 C2.41,22.99 3.50612381,23.1 4.13399899,22.8429026 L21.714504,14.0454487 C22.6563168,13.5741566 23.1272231,12.6315722 22.9702544,11.6889879 C22.8132856,11.0605983 22.3423792,10.4322088 21.714504,10.118014 L4.13399899,1.16346272 C3.34915502,0.9 2.40734225,1.00636533 1.77946707,1.4776575 C0.994623095,2.10604706 0.8376543,3.0486314 1.15159189,3.99121575 L3.03521743,10.4322088 C3.03521743,10.5893061 3.34915502,10.7464035 3.50612381,10.7464035 L16.6915026,11.5318905 C16.6915026,11.5318905 17.1624089,11.5318905 17.1624089,12.0031827 C17.1624089,12.4744748 16.6915026,12.4744748 16.6915026,12.4744748 Z"
          fill="#0084FF"
          fill-rule="evenodd"
          stroke="none"
        ></path>
      </svg>
    </v-btn>
    <v-btn icon class="ml-1" v-show="isShowIcon" @click="sendIcon">
      <svg aria-labelledby="js_1l" viewBox="0 0 16 16" height="24" width="24">
        <title id="js_1l">Ký hiệu giơ ngón tay cái</title>
        <path
          fill="#3a8df5"
          d="M16,9.1c0-0.8-0.3-1.1-0.6-1.3c0.2-0.3,0.3-0.7,0.3-1.2c0-1-0.8-1.7-2.1-1.7h-3.1c0.1-0.5,0.2-1.3,0.2-1.8 c0-1.1-0.3-2.4-1.2-3C9.3,0.1,9,0,8.7,0C8.1,0,7.7,0.2,7.6,0.4C7.5,0.5,7.5,0.6,7.5,0.7L7.6,3c0,0.2,0,0.4-0.1,0.5L5.7,6.6 c0,0-0.1,0.1-0.1,0.1l0,0l0,0L5.3,6.8C5.1,7,5,7.2,5,7.4v6.1c0,0.2,0.1,0.4,0.2,0.5c0.1,0.1,1,1,2,1h5.2c0.9,0,1.4-0.3,1.8-0.9 c0.3-0.5,0.2-1,0.1-1.4c0.5-0.2,0.9-0.5,1.1-1.2c0.1-0.4,0-0.8-0.2-1C15.6,10.3,16,9.9,16,9.1z"
        ></path>
        <path
          fill="#3a8df5"
          d="M3.3,6H0.7C0.3,6,0,6.3,0,6.7v8.5C0,15.7,0.3,16,0.7,16h2.5C3.7,16,4,15.7,4,15.3V6.7C4,6.3,3.7,6,3.3,6z"
        ></path>
      </svg>
    </v-btn>
  </div>
</template>

<script>
import axios from "axios";
import { messageType } from "@/constant";
import { mapGetters } from "vuex";
import { encryptMessage } from "@/util/cryptoMessage";

export default {
  name: "ChatDiv",
  components: {
    ImageChat: () => import("./ImageChat"),
  },
  data() {
    return {
      top: 0,
      images: [],
      imagesBase64: [],
      isChatImage: false,
      isShowIcon: true,
    };
  },
  computed: {
    ...mapGetters(["socket", "currentRoom", "code", "profile", "keyRoom"]),
    msg: {
      get() {
        return this.$store.getters.msg;
      },
      set(val) {
        this.$store.dispatch("setMsg", val);
      },
    },
    iconImage() {
      if (this.images.length > 0) return "mdi-close";
      else return "mdi-image";
    },
  },
  methods: {
    sendMsg: async function () {
      if (this.images && this.images.length > 0) {
        let formData = new FormData();
        for (let i = 0; i < this.images.length; i++) {
          formData.append("files", this.images[i]);
        }
        try {
          let { data } = await axios.post(
            `${process.env.VUE_APP_STORAGE_URL}/api/storage/multiple`,
            formData,
            {
              header: {
                "Content-Type": "multipart/form-data",
              },
            }
          );
          if (data && data.fileNames.length > 0) {
            this.msg = data.fileNames.reduce(
              (filename, msg) => (filename += `,${msg}`)
            );
            this.socket.emit(
              "sendMsg",
              JSON.stringify({
                msg: this.msg,
                roomId: this.currentRoom,
                messageType: messageType.image,
                avatarUrl: this.profile.avatarUrl,
              })
            );
            this.msg = "";
            this.images = [];
            this.imagesBase64 = [];
            this.isChatImage = false;
          }
        } catch (error) {
          console.log(error.response);
        }
      }
      if (this.$refs.chatInput.innerHTML) {
        this.msg = encryptMessage(this.$refs.chatInput.innerHTML, this.keyRoom);
        this.socket.emit(
          "sendMsg",
          JSON.stringify({
            msg: this.msg,
            roomId: this.currentRoom,
            messageType: messageType.text,
            code: this.code,
            avatarUrl: this.profile.avatarUrl,
          })
        );
        this.$refs.chatInput.innerHTML = "";
        this.msg = "";
        this.isShowIcon = true;
      }
    },
    sendIcon: async function () {
      this.socket.emit(
        "sendMsg",
        JSON.stringify({
          msg: null,
          roomId: this.currentRoom,
          messageType: messageType.icon,
          code: this.code,
          avatarUrl: this.profile.avatarUrl,
        })
      );
    },
    updateVal(val) {
      this.top = val.offsetHeight + 48;
    },
    openChooseImg() {
      if (this.isChatImage) {
        this.isChatImage = !this.isChatImage;
        this.images = [];
        this.imagesBase64 = [];
        this.setIsShowIcon();
      } else {
        this.isChatImage = !this.isChatImage;
        this.$refs["file"].click();
      }
    },
    async selectFile() {
      // this.$refs.file.files
      if (this.$refs.file.files.length > 0) {
        if (this.images.length > 0) {
          const imgFiles = [...this.$refs.file.files];
          this.images = [...this.images, ...imgFiles];
          for (let i = 0; i < imgFiles.length; i++) {
            let base64 = await this.toBase64(imgFiles[i]);
            this.imagesBase64.push(base64);
          }
        } else {
          this.images = [...this.$refs.file.files];
          for (let i = 0; i < this.images.length; i++) {
            let base64 = await this.toBase64(this.images[i]);
            this.imagesBase64.push(base64);
          }
        }
        this.setIsShowIcon();
      }
    },
    toBase64(file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
      });
    },
    setIsShowIcon() {
      const chatInput = this.$refs.chatInput;
      const val = chatInput.innerHTML.replace(/(<([^>]+)>)/gi, "");
      if (val) this.isShowIcon = false;
      else {
        if (this.images.length == 0) this.isShowIcon = true;
        else this.isShowIcon = false;
      }
    },
    onDeleteImage(index) {
      this.images.splice(index, 1);
      this.imagesBase64.splice(index, 1);
    },
    onAddImage() {
      this.$refs["file"].click();
    },
  },
  mounted() {
    this.top = this.$refs.chatInput.offsetHeight + 48;
  },
  watch: {
    imagesBase64(newVal) {
      if (newVal.length == 0) {
        this.isChatImage = true;
        this.isChatImage = false;
      }
    },
  },
};
</script>

<style>
.chat-content-bottom {
  border-bottom-left-radius: 0.375rem;
  border-bottom-right-radius: 0.375rem;
}
.chat-div {
  width: calc(100% - 88px);
}
.input-div {
  border: none;
  outline: none;
  border-radius: 20px;
  background-color: #37424a;
  bottom: 0;
  padding: 8px 16px;
  padding-top: 10px;
  min-height: 40px;
  max-height: 80px;
  z-index: 100;
}
.input-div:empty:before {
  content: attr(data-placeholder);
  color: gray;
}
.input-div:hover {
  overflow-y: overlay;
}
.input-div {
  overflow-y: hidden;
}

.input-div::-webkit-scrollbar {
  width: 8px;
}

.input-div::-webkit-scrollbar-track:hover {
  border-radius: 0.375rem;
  background-color: #7a7a7a;
}

.input-div::-webkit-scrollbar-thumb {
  border-radius: 0.375rem;
  background-color: #575757;
}
.add-image {
  background-color: #868e9940;
}

.image-list {
  background-color: #37424a;
  border-radius: 0.375rem;
  grid-gap: 16px;
  z-index: 100;
  overflow-x: hidden;
  overflow-y: hidden;
}
.image-list:hover {
  overflow-x: overlay;
}

.image-list::-webkit-scrollbar {
  height: 8px;
}

.image-list::-webkit-scrollbar-track:hover {
  border-radius: 0.375rem;
  background-color: #7a7a7a;
}

.image-list::-webkit-scrollbar-thumb {
  border-radius: 0.375rem;
  background-color: #575757;
}
</style>
