<template>
  <div
    class="intro-y overflow-y-auto scrollbar-hidden pt-2 mt-3 -mx-5 px-5"
    style="height: calc(100% - 75px)"
  >
    <div v-if="!isEmpty" class="text-gray-500 mt-4 text-center">
      Không tìm thấy tin nhắn
    </div>
    <v-list-item-group mandatory>
      <v-list-item
        :class="`${
          currentRoom == room.conversationId && `chat-list--active`
        } chat-list cursor-pointer relative flex items-center px-4 py-3 zoom-in mt-2`"
        v-for="room in rooms"
        :key="room.conversationId"
        @click="joinRoom(room.conversationId, room.id)"
      >
        <RoomItem :room="room" />
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
import { messageType } from "@/constant";

export default {
  name: "ChatList",
  components: {
    RoomItem: () => import("./RoomItem.vue"),
  },
  data: () => {
    return {
      loaddingRoom: true,
      isEmpty: true,
      messageType: messageType,
    };
  },
  computed: {
    ...mapGetters(["socket", "rooms", "accessToken", "currentRoom"]),
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
      if (this.currentRoom != id) {
        this.saveCurrentRoom(id);
        this.loadMessages();
        this.setFriendProfile({
          accessToken: this.accessToken,
          userId: userId,
        });
      }
    },
  },
  watch: {
    rooms: function (newVal) {
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
