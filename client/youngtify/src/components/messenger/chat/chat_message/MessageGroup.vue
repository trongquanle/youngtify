<template>
  <div>
    <div class="clear-both"></div>
    <div
      :class="self ? 'intro-x float-right' : '-intro-x float-left'"
      class="w-full chat-text-box flex items-end mb-2"
    >
      <div
        v-if="!self"
        class="chat-text-box__photo hidden sm:block flex-none image-fit relative mr-2 ml-2"
      >
        <v-avatar
          class="cursor-pointer absolute top-26"
          v-if="avatar"
          size="32"
        >
          <v-img :src="avatar" :alt="avatar"></v-img>
        </v-avatar>
        <v-avatar
          v-else
          class="cursor-pointer absolute top-26"
          color="#4B5563"
          size="32"
        >
          <span class="primary--text">LT</span>
        </v-avatar>
      </div>
      <div :class="!self || 'mr-2'" class="w-full">
        <div v-for="msg in message" :key="msg.id">
          <div class="clear-both mb-2"></div>
          <Message :self="self" :msg="msg" />
        </div>
        <div class="clear-both"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "MessageGroup",
  props: {
    message: Array,
  },
  components: {
    Message: () => import("./Message"),
  },
  computed: {
    ...mapGetters(["code"]),
    self() {
      return this.message[0]["senderId"] == this.code ? true : false;
    },
    avatar() {
      if (this.message[0].avatarUrl) {
        if (this.message[0].avatarUrl.search("https") !== -1)
          return this.message[0].avatarUrl;
        else
          return `${process.env.VUE_APP_STORAGE_URL}${this.message[0].avatarUrl}`;
      } else return undefined;
    },
  },
};
</script>

<style>
.top-26 {
  top: 26px;
}
</style>