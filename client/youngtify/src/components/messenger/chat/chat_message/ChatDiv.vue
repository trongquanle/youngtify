<template>
  <div
    class="w-full chat-content-bottom bc-cpn-global p-2 flex items-center justify-between relative"
  >
    <v-btn class="mr-2" icon color="#3a8df5" @click="openChooseImg">
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
        <ImageChat v-for="(image, i) in imagesBase64" :key="i" :image="image" />
        <div class="add-image w-12 h-12 br-global cursor-pointer" v-ripple>
          <v-icon size="48">mdi-image-plus</v-icon>
        </div>
      </div>
      <div
        contenteditable="true"
        data-placeholder="Aa"
        class="absolute input-div w-full cursor-pointer"
        ref="chatInput"
        @input="updateVal($event.target)"
      >
        <!-- <div contenteditable="false">12gyudwvd</div> -->
        <!-- <div>12gyudwvd</div> -->
      </div>
    </div>
    <v-btn icon class="ml-2" color="#3a8df5" @click="sendMsg">
      <v-icon size="28">mdi-send</v-icon>
    </v-btn>
  </div>
</template>

<script>
import axios from "axios";

import { mapGetters } from "vuex";

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
    };
  },
  computed: {
    ...mapGetters(["socket", "currentRoom"]),
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
    sendMsg: async function() {
      if (this.images && this.images.length > 0) {
        let formData = new FormData();
        for (let i = 0; i < this.images.length; i++) {
          formData.append("files", this.images[i]);
        }
        try {
          let { data } = await axios.post(
            "http://localhost:3000/api/storage/multiple",
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
                messageType: 1,
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
        this.msg = this.$refs.chatInput.innerHTML;
        this.socket.emit(
          "sendMsg",
          JSON.stringify({
            msg: this.msg,
            roomId: this.currentRoom,
            messageType: 0,
          })
        );
        this.$refs.chatInput.innerHTML = "";
        this.msg = "";
      }
    },
    updateVal(val) {
      this.top = val.offsetHeight + 48;
    },
    openChooseImg() {
      if (this.isChatImage) {
        this.isChatImage = !this.isChatImage;
        this.images = [];
        this.imagesBase64 = [];
      } else {
        this.isChatImage = !this.isChatImage;
        this.$refs["file"].click();
      }
    },
    async selectFile() {
      if (this.$refs.file.files.length > 0) {
        this.images = this.$refs.file.files;
        for (let i = 0; i < this.images.length; i++) {
          let base64 = await this.toBase64(this.images[i]);
          this.imagesBase64.push(base64);
        }
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
  },
  mounted() {
    this.top = this.$refs.chatInput.offsetHeight + 48;
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
