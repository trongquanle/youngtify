<template>
  <div
    class="-intro-x w-full h-full flex items-center cursor-pointer"
    @mouseover="active = true"
    @mouseleave="active = false"
    @click="joinRoom"
  >
    <div class="card-friend-item ml-4 flex items-center">
      <v-badge
        bordered
        bottom
        color="green accent-4"
        dot
        offset-x="10"
        offset-y="10"
      >
        <v-avatar v-if="user.avatar" size="48">
          <v-img src="https://cdn.vuetifyjs.com/images/lists/2.jpg"></v-img>
        </v-avatar>
        <v-avatar v-else color="#4B5563" size="48">
          <span class="primary--text headline font-bold">{{ name }}</span>
        </v-avatar>
      </v-badge>
      <div class="ml-4">{{ user.fullname }}</div>
    </div>
    <v-btn v-show="active" class="mr-4" icon color="#3a8df5">
      <v-icon size="24">mdi-dots-horizontal</v-icon>
    </v-btn>
  </div>
</template>

<script>
import { request } from "@/api";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "Friend",
  props: {
    user: {
      type: Object,
    },
  },
  data() {
    return {
      active: false,
    };
  },
  computed: {
    ...mapGetters(["accessToken"]),
    name() {
      let names = this.user.fullname.split(" ");
      let temp = names[0][0] + names[names.length - 1][0];
      return temp;
    },
  },
  methods: {
    ...mapActions(["saveCurrentRoom", "loadMessages", "setFriendProfile"]),
    joinRoom: async function () {
      try {
        let { data } = await request(`/api/conversations/${this.user.id}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${this.accessToken}`,
          },
        });
        if (data && data.roomId) {
          this.saveCurrentRoom(data.roomId);
          this.loadMessages();
          this.setFriendProfile({accessToken: this.accessToken, userId: this.user.id});
        }
      } catch (error) {
        console.log(error.response);
      }
    },
  },
};
</script>

<style>
</style>