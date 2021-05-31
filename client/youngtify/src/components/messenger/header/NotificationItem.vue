<template>
  <div
    class="cursor-pointer relative flex items-center mt-2 p-2 dark-hover br-global"
    v-ripple
  >
    <div class="w-10 h-10 flex-none image-fit mr-1">
      <v-avatar v-if="avatar" :size="40">
        <img class="object-fit-cover" :src="avatar" />
      </v-avatar>
      <v-avatar v-else color="#4B5563" :size="40">
        <span class="primary--text headline">{{ name }}</span>
      </v-avatar>
    </div>
    <div
      :class="`ml-2 overflow-hidden w-full ${notify.isSeen && 'text-gray-500'}`"
    >
      <div class="flex items-center">
        <a class="font-medium truncate mr-5">{{ notify.fullName }}</a>
        <div class="text-opacity-50 text-xs ml-auto whitespace-nowrap -mt-0.5">
          {{ timeText }}
        </div>
      </div>
      <div class="text-opacity-70 w-full mt-0.5">
        {{ notify.content }}
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import moment from "moment";

export default {
  name: "NotificationItem",
  props: {
    notify: {
      type: Object,
    },
  },
  computed: {
    ...mapGetters(["currentTime"]),
    timeText() {
      let text = "";
      const duration = moment.duration(
        this.currentTime.diff(this.notify.createdDate)
      );
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
    avatar(){
      if (this.notify.avatarUrl) {
        if (this.notify.avatarUrl.search("https://") !== -1) return this.notify.avatarUrl;
        return `${process.env.VUE_APP_STORAGE_URL}${this.notify.avatarUrl}`;
      }
      return undefined;
    },
    name() {
      let names = this.notify.fullName.split(" ");
      return `${names[0][0]}${names[names.length - 1][0]}`;
    }
  },
};
</script>

<style>
</style>