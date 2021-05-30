<template>
  <div class="w-full flex">
    <Avatar :id="room.id" :title="room.title" :avatarUrl="room.avatarUrl" />
    <div class="ml-2 overflow-hidden w-4/5">
      <a class="chat-list__name text-base font-medium truncate">{{
        room.title
      }}</a>
      <div class="flex items-center text-xs">
        <div
          class="chat-list__text w-4/5 truncate"
          v-if="room.messageType == messageType.text"
        >
          <span>{{ actorRoom }}:</span>
          {{ msgTitle }}
        </div>
        <div
          class="chat-list__text w-4/5 truncate"
          v-else-if="room.messageType == messageType.image"
        >
          <span>{{ actorRoom }}:</span>
          {{ room.messageTitle }}
        </div>
        <div class="chat-list__text w-4/5 truncate flex" v-else>
          <span class="mr-1">{{ actorRoom }}:</span>
          <img
            height="16"
            width="16"
            alt="(Y)"
            referrerpolicy="origin-when-cross-origin"
            src="https://static.xx.fbcdn.net/images/emoji.php/v9/e40/1/16/LIKE.png"
          />
        </div>
      </div>
    </div>
    <div class="flex flex-col">
      <div
        class="chat-list__time whitespace-no-wrap text-opacity-80 text-xs text-gray-800 dark:text-gray-600"
      >
        {{ timeText }}
      </div>
      <div
        class="chat-list__action dropdown transition duration-200 opacity-0 mt-1 -mb-1 -mr-1 ml-auto"
        style="position: relative"
      >
        <a
          class="dropdown-toggle block text-opacity-70 text-gray-500"
          href="javascript:;"
          aria-expanded="true"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="feather feather-chevron-down w-6 h-6"
          >
            <polyline points="6 9 12 15 18 9"></polyline>
          </svg>
        </a>
        <div data-dropdown-replacer="_n76gs8vs9"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import moment from "moment";
import { messageType } from "@/constant";
import { getKey, decryptMessage } from "@/util/cryptoMessage";

export default {
  name: "RoomItem",
  props: {
    room: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      messageType: messageType,
      keyRoom: undefined
    };
  },
  components: {
    Avatar: () => import("./Avatar.vue"),
  },
  computed: {
    ...mapGetters([
      "currentTime",
      "code",
    ]),
    timeText() {
      let text = "";
      const duration = moment.duration(this.currentTime.diff(this.room.modifiedDate));
      if (duration.asMinutes() < 60) {
        let minute = parseInt(duration.asMinutes());
        text = `${minute == 0 ? 1 : minute} phút`;
      } else if (duration.asHours() < 24) {
        text = `${parseInt(duration.asHours())} giờ`;
      } else if (duration.asDays() < 7) {
        text = `${parseInt(duration.asDays())} ngày`;
      } else if (duration.asWeeks() < 4) {
        text = `${parseInt(duration.asWeeks())} tuần`;
      } else if (duration.asMonths() < 12) {
        text = `${parseInt(duration.asMonths())} tháng`;
      } else {
        text = `${parseInt(duration.asYears())} năm`;
      }
      return text;
    },
    msgTitle() {
      const plantText = decryptMessage(this.room.messageTitle, this.keyRoom);
      if (plantText)
        return plantText.replace(/(<([^>]+)>)/gi, " ").trim();
    },
    actorRoom() {
      if (this.code === this.room.senderId) {
        return "Bạn";
      } else {
        let names = this.room.title.split(" ");
        return `${names[names.length - 1]}`;
      }
    },
  },
  created(){
    this.keyRoom = getKey(this.room.conversationId);
  }
};
</script>

<style>
</style>