<template>
  <div
    ref="chat"
    class="chat-content overflow-y-scroll overflow-x-hidden scrollbar-hidden pt-3 flex-1 flex flex-col-reverse"
    v-on:scroll="handleScroll"
  >
    <div class="clear-both"></div>
    <div v-if="loadding">
      <div :style="`width: 100%;`" v-for="(sk, i) in skelectons" v-bind:key="i">
        <v-sheet
          :class="`${sk.left ? 'float-right pr-2' : 'float-left pl-2'} mb-3`"
          :style="`width: ${sk.width}px; border-radius: 0.375rem;`"
        >
          <v-skeleton-loader
            class="mx-auto"
            :type="sk.left ? 'list-item' : 'list-item-avatar'"
            :dark="true"
          ></v-skeleton-loader>
        </v-sheet>
        <div class="clear-both"></div>
      </div>
    </div>
    <div class="clear-both mb-2"></div>
    <MessageGroup v-for="m in messages" :key="m.id" :message="m.data" />
    <div v-if="loadMore" class="align-center justify-center d-flex py-2">
      <v-progress-circular
        :size="28"
        color="primary"
        indeterminate
      ></v-progress-circular>
    </div>
    <!-- <div class="clear-both"></div>
    <div class="intro-x chat-text-box flex items-end float-right mb-4">
      <div class="mr-2 w-full">
        <div>
          <div class="chat-text-box__content flex items-center float-right">
            <div class="hidden sm:block dropdown relative mr-3 mt-3">
              <a
                href="javascript:;"
                class="dropdown-toggle w-4 h-4"
                aria-expanded="false"
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
                  class="feather feather-more-vertical w-4 h-4"
                >
                  <circle cx="12" cy="12" r="1"></circle>
                  <circle cx="12" cy="5" r="1"></circle>
                  <circle cx="12" cy="19" r="1"></circle>
                </svg>
              </a>
            </div>
            <div
              class="box leading-relaxed bg-theme-1 text-opacity-80 text-white px-4 py-3 mt-3"
            >
              Lorem ipsum sit
              <a class="text-white" href="">@morganfreeman</a> amen dolor, lorem
              ipsum sit amen dolor
            </div>
          </div>
          <div class="clear-both"></div>
          <div class="chat-text-box__content flex items-center float-right">
            <div class="hidden sm:block dropdown relative mr-3 mt-3">
              <a
                href="javascript:;"
                class="dropdown-toggle w-4 h-4"
                aria-expanded="false"
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
                  class="feather feather-more-vertical w-4 h-4"
                >
                  <circle cx="12" cy="12" r="1"></circle>
                  <circle cx="12" cy="5" r="1"></circle>
                  <circle cx="12" cy="19" r="1"></circle>
                </svg>
              </a>
            </div>
            <div
              class="box leading-relaxed bg-theme-1 text-opacity-80 text-white px-4 py-3 mt-3"
            >
              Lorem ipsum sit
              <a class="text-white" href="">@morganfreeman</a> amen dolor, lorem
              ipsum sit amen dolor
            </div>
          </div>
          <div class="clear-both"></div>
          <div class="chat-text-box__content flex items-center float-right">
            <div class="hidden sm:block dropdown relative mr-3 mt-3">
              <a
                href="javascript:;"
                class="dropdown-toggle w-4 h-4"
                aria-expanded="false"
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
                  class="feather feather-more-vertical w-4 h-4"
                >
                  <circle cx="12" cy="12" r="1"></circle>
                  <circle cx="12" cy="5" r="1"></circle>
                  <circle cx="12" cy="19" r="1"></circle>
                </svg>
              </a>
            </div>
            <div class="rounded-md grid gap-2 grid-cols-4 mt-3">
              <div class="image-fit zoom-in">
                <v-img
                  class="cursor-pointer h-16 w-16 br-global"
                  src="@/assets/img/profile-7.jpg"
                ></v-img>
              </div>
              <div class="image-fit zoom-in">
                <v-img
                  class="cursor-pointer h-16 w-16 br-global"
                  src="@/assets/img/profile-7.jpg"
                ></v-img>
              </div>
              <div class="image-fit zoom-in">
                <v-img
                  class="cursor-pointer h-16 w-16 br-global"
                  src="@/assets/img/profile-7.jpg"
                ></v-img>
              </div>
              <div class="image-fit zoom-in">
                <v-img
                  class="cursor-pointer h-16 w-16 br-global"
                  src="@/assets/img/profile-3.jpg"
                ></v-img>
              </div>
              <div class="image-fit zoom-in">
                <v-img
                  class="cursor-pointer h-16 w-16 br-global"
                  src="@/assets/img/profile-7.jpg"
                ></v-img>
              </div>
              <div class="image-fit zoom-in">
                <v-img
                  class="cursor-pointer h-16 w-16 br-global"
                  src="@/assets/img/profile-7.jpg"
                ></v-img>
              </div>
              <div class="image-fit zoom-in">
                <v-img
                  class="cursor-pointer h-16 w-16 br-global"
                  src="@/assets/img/profile-3.jpg"
                ></v-img>
              </div>
            </div>
          </div>
        </div>
        <div class="clear-both mb-2"></div>
        <div class="text-gray-600 text-xs text-right">1 mins ago</div>
      </div>
    </div>
    <div class="clear-both"></div>
    <div class="-intro-x chat-text-box flex items-end float-left mb-4">
      <div
        class="w-8 h-8 hidden sm:block flex-none image-fit relative mr-2 ml-2"
      >
        <img
          alt="Topson Messenger Tailwind HTML Admin Template"
          class="rounded-full"
          src="@/assets/img/profile-10.jpg"
        />
      </div>
      <div class="w-full">
        <div>
          <div class="chat-text-box__content flex items-center float-left">
            <div class="chat-text-box__content__text px-3 py-2">
              Robert De Niro is typing
              <span class="typing-dots ml-1">
                <span>.</span> <span>.</span> <span>.</span>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div> -->
  </div>
</template>

<script>
import MessageGroup from "./MessageGroup";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "ChatContent",
  components: {
    MessageGroup,
  },
  data: () => {
    return {
      items: [
        {
          icon: "mdi-reply ",
          text: "Trả lời",
        },
        {
          icon: "mdi-delete",
          text: "Xóa",
        },
      ],
      model: 1,
      scrollIndex: 0,
      skelectons: [
        { left: true, width: 300 },
        { left: true, width: 350 },
        { left: true, width: 320 },
        { left: false, width: 400 },
        { left: false, width: 350 },
        { left: false, width: 370 },
        { left: false, width: 326 },
        { left: true, width: 330 },
        { left: true, width: 450 },
        { left: false, width: 380 },
      ],
    };
  },
  computed: {
    ...mapGetters([
      "currentRoom",
      "messages",
      "key",
      "scrollDown",
      "loadMore",
      "loadding",
      "loadMore",
    ]),
  },
  // created() {
  //   console.log(this.messages);
  //   let msgts = [];
  //   let actor = [];
  //   let id;
  //   this.messages.forEach((msg, index) => {
  //     console.log(msg.senderId);
  //     if (!id) {
  //       id = msg.senderId;
  //       actor.push(msg);
  //     } else if (id == msg.senderId) {
  //       actor.push(msg);
  //       if (index == this.messages.length-1) {
  //         msgts.push(actor);
  //       }
  //     } else {
  //       msgts.push(actor);
  //       actor = [];
  //       id = undefined;
  //     }
  //   });
  //   console.log(msgts);
  // },
  methods: {
    ...mapActions(["loadMoreMessages", "changeMsgTitle"]),
    handleScroll(e) {
      if (this.loadMore && this.key) {
        const chatDiv = this.$refs.chat;
        const maxScrollY = chatDiv.scrollHeight - chatDiv.clientHeight;
        if (chatDiv.scrollTop == -maxScrollY){
          this.loadMoreMessages();
        }
      }
    },
  },
  mounted() {
    this.$refs.chat.scrollTop = this.$refs.chat.scrollHeight;
  },
  watch: {
    messages: function (newVal) {
      if (this.scrollDown) {
        setTimeout(() => {
          this.$refs.chat.scrollTop = 0;
        }, 0);
      }
    },
    $route(to) {
      if (to && to.name == "youngtify") {
        this.$refs.chat.scrollTop = 0;
      }
    },
  },
};
</script>

<style>
.chat-content {
  height: calc(100% - 144px);
  background-color: #232a3b;
}
.-intro-x .chat-text-box__content__text {
  background-color: #313a55 !important;
}

*,
*:before,
*:after {
  box-sizing: border-box;
}

.dashed-loading {
  position: relative;
  height: 32px;
}
.dashed-loading:after,
.dashed-loading:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  border-radius: 50%;
  width: 32px;
  height: 32px;
}
.dashed-loading:before {
  z-index: 5;
  border: 3px dashed #08aeea;
  border-left: 3px solid transparent;
  border-bottom: 3px solid transparent;
  -webkit-animation: dashed 1s linear infinite;
  animation: dashed 1s linear infinite;
}
.dashed-loading:after {
  z-index: 10;
  border: 3px solid #08aeea;
  border-left: 3px solid transparent;
  border-bottom: 3px solid transparent;
  -webkit-animation: dashed 1s ease infinite;
  animation: dashed 1s ease infinite;
}
@keyframes dashed {
  to {
    transform: rotate(360deg);
  }
}
</style>
