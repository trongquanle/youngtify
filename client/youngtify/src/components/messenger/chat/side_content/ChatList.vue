<template>
  <div
    class="intro-y overflow-y-auto scrollbar-hidden pt-2 mt-3 -mx-5 px-5"
    style="height: calc(100% - 75px)"
  >
    <v-list-item-group mandatory>
      <v-list-item
        class="chat-list cursor-pointer relative flex items-center px-4 py-3 zoom-in mt-2"
        v-for="room in rooms"
        :key="room.conversationId"
        active-class="chat-list--active"
        @click="joinRoom(room.conversationId, room.id)"
      >
        <v-badge
          bordered
          bottom
          color="white-black"
          dot
          offset-x="10"
          offset-y="10"
        >
          <v-avatar v-if="room.avatarUrl" size="48">
            <img
              class="object-fit-cover"
              :src="`http://localhost:3000${room.avatarUrl}`"
            />
          </v-avatar>
          <v-avatar v-else color="#4B5563" size="48">
            <span class="primary--text headline">{{ name(room.title) }}</span>
          </v-avatar>
        </v-badge>
        <div class="ml-2 overflow-hidden w-4/5">
          <a class="chat-list__name text-base font-medium truncate">{{
            room.title
          }}</a>
          <div class="flex items-center text-xs">
            <div class="chat-list__text w-4/5 truncate">
              {{ msgTitle(room.messageTitle) }}
            </div>
          </div>
        </div>
        <div class="flex flex-col">
          <div
            class="chat-list__time whitespace-no-wrap text-opacity-80 text-xs text-gray-800 dark:text-gray-600"
          >
            01:10 PM
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
      </v-list-item>
    </v-list-item-group>
    <div class="mt-2" v-if="loaddingRoom">
      <div v-for="i in 10" v-bind:key="i">
        <v-sheet :color="`grey lighten-4`" class="mb-3 rounded-lg">
          <v-skeleton-loader
            class="mx-auto"
            max-width="310"
            type="list-item-avatar-two-line"
          ></v-skeleton-loader>
        </v-sheet>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  name: "ChatList",
  data: () => {
    return {
      loaddingRoom: true,
    };
  },
  computed: {
    ...mapGetters(["socket", "rooms", "accessToken"]),
  },
  async created() {
    this.loadRoom();
  },
  methods: {
    ...mapActions([
      "saveCurrentRoom",
      "loadRoom",
      "loadMessages",
      "setFriendProfile",
    ]),
    joinRoom(id, userId) {
      this.saveCurrentRoom(id);
      this.loadMessages();
      this.setFriendProfile({ accessToken: this.accessToken, userId: userId });
    },
    name(title) {
      let names = title.split(" ");
      return `${names[0][0]}${names[names.length - 1][0]}`;
    },
    msgTitle(title) {
      if (title) return title.replace(/(<([^>]+)>)/gi, " ").trim();
    },
  },
  watch: {
    rooms: function(newVal) {
      if (newVal) this.loaddingRoom = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.px-4 {
  padding-left: 1rem;
  padding-right: 1rem;
}
.py-3 {
  padding-top: 0.75rem;
  padding-bottom: 0.75rem;
}
</style>
