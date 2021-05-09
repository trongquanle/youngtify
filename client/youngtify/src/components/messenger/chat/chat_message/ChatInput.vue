<template>
  <div class="intro-x chat-input flex items-center px-2 py-2">
    <!-- BEGIN: Chat Input Dropdown -->
    <div class="dropdown relative">
      <v-btn icon color="#3a8df5">
        <v-icon size="28">mdi-plus</v-icon>
      </v-btn>
      <div class="chat-input__dropdown dropdown-box absolute top-0 left-0 -ml-4 z-20">
        <div class="dropdown-box__content p-2">
          <a
            href=""
            class="chat-input__dropdown__item flex items-center block p-3 transition duration-300 rounded-md mb-2"
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
              class="feather feather-camera w-5 h-5"
            >
              <path
                d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"
              ></path>
              <circle cx="12" cy="13" r="4"></circle>
            </svg>
          </a>
          <a
            href=""
            class="chat-input__dropdown__item flex items-center block p-3 transition duration-300 rounded-md mb-2"
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
              class="feather feather-mic w-5 h-5"
            >
              <path
                d="M12 1a3 3 0 0 0-3 3v8a3 3 0 0 0 6 0V4a3 3 0 0 0-3-3z"
              ></path>
              <path d="M19 10v2a7 7 0 0 1-14 0v-2"></path>
              <line x1="12" y1="19" x2="12" y2="23"></line>
              <line x1="8" y1="23" x2="16" y2="23"></line>
            </svg>
          </a>
          <a
            href=""
            class="chat-input__dropdown__item flex items-center block p-3 transition duration-300 rounded-md mb-2"
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
              class="feather feather-mail w-5 h-5"
            >
              <path
                d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"
              ></path>
              <polyline points="22,6 12,13 2,6"></polyline>
            </svg>
          </a>
        </div>
      </div>
    </div>
    <!-- END: Chat Input Dropdown -->
    <textarea
      class="input w-full h-16 resize-none border-transparent px-2 focus:shadow-none truncate mr-2 sm:mr-0"
      rows="1"
      placeholder="Type your message..."
      v-model="msg"
      @keyup.prevent.enter="sendMsg"
    ></textarea>
    <!-- BEGIN: Chat Smiley Dropdown -->
    <div class="dropdown relative">
      <a href="javascript:;" class="chat-input__action dropdown-toggle w-4 h-4 sm:w-5 sm:h-5 block">
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
          class="feather feather-smile w-full h-full"
        >
          <circle cx="12" cy="12" r="10"></circle>
          <path d="M8 14s1.5 2 4 2 4-2 4-2"></path>
          <line x1="9" y1="9" x2="9.01" y2="9"></line>
          <line x1="15" y1="9" x2="15.01" y2="9"></line>
        </svg>
      </a>
    </div>
    <!-- END: Chat Smiley Dropdown -->

    <v-btn icon class="ml-2" color="#3a8df5" @click="sendMsg">
      <v-icon size="28">mdi-send</v-icon>
    </v-btn>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
// import moment from 'moment';

export default {
  name: "ChatInput",
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
  },
  methods: {
    sendMsg: function () {
      if (this.msg) {
        this.socket.emit("sendMsg", JSON.stringify({msg: this.msg, roomId: this.currentRoom}));
        this.msg = "";
      }
    },
  },
};
</script>

<style>
</style>