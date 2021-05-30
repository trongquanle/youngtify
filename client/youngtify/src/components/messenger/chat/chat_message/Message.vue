<template>
  <!-- <div class="clear-both mb-2"></div> -->
  <div
    :class="self ? 'intro-x float-right justify-end' : '-intro-x float-left'"
    class="w-full chat-text-box__content flex items-center transition duration-300 ease-in-out"
    @mouseenter="isShowAction = true"
    @mouseleave="isShowAction = false"
  >
    <MessageAction
      v-show="isShowAction && !isShowDivTime"
      v-if="self"
      :self="self"
    />
    <!-- type text -->
    <v-tooltip
      v-if="msg.messageType == messageType.text"
      :right="!self"
      :left="self"
    >
      <template v-slot:activator="{ on, attrs }">
        <div
          :class="!self || 'chat-text-box__content__text--primary'"
          class="chat-text-box__content__text px-3 py-2"
          v-html="decryptMsg"
          v-bind="attrs"
          v-on="on"
        ></div>
      </template>
      <span>{{ timeText }}</span>
    </v-tooltip>
    <!-- type image -->
    <v-tooltip
      v-else-if="msg.messageType == messageType.image"
      :right="!self"
      :left="self"
    >
      <template v-slot:activator="{ on, attrs }">
        <div
          :class="`image-chat rounded-md justify-${self ? 'end' : 'start'}`"
          v-bind="attrs"
          v-on="on"
        >
          <div v-for="(img, i) in images" :key="img" class="image-fit zoom-in">
            <v-img
              @click="showSlideImages(i)"
              class="cursor-pointer h-24 w-24 br-global"
              :src="`${VUE_APP_STORAGE_URL}${img}`"
            ></v-img>
          </div>
        </div>
      </template>
      <span>{{ timeText }}</span>
    </v-tooltip>
    <v-tooltip
      v-else-if="msg.messageType == messageType.icon"
      :right="!self"
      :left="self"
    >
      <template v-slot:activator="{ on, attrs }">
        <div
          :class="`justify-${self ? 'end' : 'start'} h-9`"
          v-bind="attrs"
          v-on="on"
        >
          <svg
            aria-labelledby="js_1l"
            viewBox="0 0 16 16"
            height="36"
            width="36"
          >
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
        </div>
      </template>
      <span>{{ timeText }}</span>
    </v-tooltip>
    <MessageAction
      v-show="isShowAction && !isShowDivTime"
      v-if="!self"
      :self="self"
    />
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import moment from "moment";
import { messageType } from "@/constant";
import { decryptMessage } from "@/util/cryptoMessage";

export default {
  name: "Message",
  props: {
    self: {
      type: Boolean,
    },
    msg: Object,
  },
  components: {
    MessageAction: () => import("./MessageAction"),
  },
  data() {
    return {
      isShowAction: false,
      width: 0,
      isShowDivTime: false,
      messageType: messageType,
      VUE_APP_STORAGE_URL: process.env.VUE_APP_STORAGE_URL,
    };
  },
  computed: {
    ...mapGetters(["currentTime", "imgs", "keyRoom"]),
    decryptMsg() {
      return decryptMessage(this.msg.message, this.keyRoom);
    },
    images() {
      if (this.msg.message) return this.msg.message.split(",");
      else return [];
    },
    timeText() {
      let time = moment(this.msg.modifiedDate);
      const duration = moment.duration(this.currentTime.diff(time));
      if (duration.asSeconds() < 60) {
        return "Vừa xong";
      } else if (duration.asMinutes() < 60) {
        let minute = parseInt(duration.asMinutes());
        return `${minute == 0 ? 1 : minute} phút trước`;
      } else if (duration.asHours() < 24) {
        if (this.currentTime.day() == time.day())
          return `${this.padleft(time.hours())}:${this.padleft(time.minute())}`;
        else
          return `${this.getDayOfWeek(time.weekday())} lúc ${this.padleft(
            time.hours()
          )}:${this.padleft(time.minute())}`;
      } else if (duration.asDays() < 7) {
        return `${this.getDayOfWeek(time.weekday())} lúc ${this.padleft(
          time.hours()
        )}:${this.padleft(time.minute())}`;
      } else {
        return `${this.padleft(time.hours())}:${this.padleft(time.minute())} ${
          time.day() + 1
        } Tháng ${time.month() + 1}, ${time.year()}`;
      }
      // return this.msg.modifiedDate;
    },
    visible: {
      get() {
        return this.$store.getters.visible;
      },
      set(val) {
        this.$store.dispatch("setVisible", val);
      },
    },
  },
  methods: {
    ...mapActions(["setImages"]),
    getDayOfWeek(day) {
      switch (day) {
        case 1:
          return "Thứ hai";
        case 2:
          return "Thứ ba";
        case 3:
          return "Thứ tư";
        case 4:
          return "Thứ năm";
        case 5:
          return "Thứ sáu";
        case 6:
          return "Thứ bảy";
        default:
          return "Chủ nhật";
      }
    },
    padleft(val) {
      return val.toString().padStart(2, "0");
    },
    showSlideImages(index) {
      this.setImages({ imgs: this.images, index });
      this.visible = true;
    },
  },
};
</script>

<style>
.image-chat {
  grid-gap: 8px;
  max-width: 304px;
  display: flex;
  flex-wrap: wrap;
}
.y-chat-time {
  background-color: #dbd9d9;
  opacity: 0.95;
}
</style>
