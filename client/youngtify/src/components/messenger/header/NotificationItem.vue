<template>
  <div
    class="cursor-pointer relative flex items-center mt-2 p-2 dark-hover br-global"
    v-ripple
  >
    <div class="w-10 h-10 flex-none image-fit mr-1">
      <img
        alt="Topson Messenger Tailwind HTML Admin Template"
        class="rounded-full"
        src="@/assets/img/profile-1.jpg"
      />
    </div>
    <div :class="`ml-2 overflow-hidden w-full ${notify.isSeen && 'text-gray-500'}`">
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
import moment from 'moment';

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
        this.currentTime.diff(this.notify.modifiedDate)
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
  },
};
</script>

<style>
</style>