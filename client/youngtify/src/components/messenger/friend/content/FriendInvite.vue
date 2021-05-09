<template>
  <div class="h-full w-full pt-4 pb-4 pr-4 text-dark">
    <div
      class="y-max-width-content y-scrollbar w-full grid justify-items-stretch"
    >
      <div class="-intro-x w-4/5 pb-4 justify-self-center text-base font-bold">
        Lời mời kết bạn (1)
      </div>
      <FriendCard v-for="user in friendInvites" :key="user.id" :user="user" :action="3" />
    </div>
    <div
      style="height: calc(100% - 40px)"
      class="intro-x w-full flex items-center justify-center"
      v-if="!friendInvites"
    >
      <div class="grid justify-items-stretch">
        <img
          class="justify-self-center"
          src="@/assets/img/empty-friend-request.png"
          width="350"
          height="105"
          alt="empty-friend-request.png"
        />
        <div class="justify-self-center mt-4 text-gray-500">
          Bạn chưa có lời mời kết bạn nào
        </div>
        <v-btn class="justify-self-center mt-4" @click="$router.push({name: 'search'})" color="primary">
          <span class="normal-case text-dark">Thêm bạn</span>
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import { request } from "@/api";
import { mapGetters } from "vuex";

export default {
  name: "FriendInvite",
  components: {
    FriendCard: () => import("./FriendCard.vue"),
    FriendSkeleton: () => import("./FriendSkeleton.vue"),
  },
  data() {
    return {
      friendInvites: undefined,
      key: "",
    };
  },
  computed: { ...mapGetters(["accessToken"]) },
  async created() {
    try {
      let { data } = await request("/api/friends/request", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${this.accessToken}`,
        },
      });
      if (data) {
        this.friendInvites = data.items;
        this.key = data.key;
      }
    } catch (error) {
      console.log(error.response);
    }
  },
};
</script>

<style>
</style>