<template>
  <div class="h-full p-4">
    <div class="f-side-content br-global text-dark h-full w-full">
      <router-link
        tag="div"
        :to="{ name: 'search' }"
        class="-intro-y w-full h-10 dark-hover br-global flex items-center justify-center cursor-pointer transition duration-300 ease-in-out"
        exact-active-class="f-active"
        v-ripple
      >
        <div class="pr-4">
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
            class="feather feather-user-plus w-5 h-5 mx-auto"
          >
            <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="8.5" cy="7" r="4"></circle>
            <line x1="20" y1="8" x2="20" y2="14"></line>
            <line x1="23" y1="11" x2="17" y2="11"></line>
          </svg>
        </div>
        <div>Thêm mới bạn bè</div>
      </router-link>
      <router-link
        tag="div"
        :to="{ name: 'requests' }"
        class="-intro-y w-full h-16 dark-hover br-global flex items-center cursor-pointer transition duration-300 ease-in-out"
        exact-active-class="f-active"
        v-ripple
      >
        <div class="pl-4">
          <img
            src="@/assets/img/friend.png"
            class="w-12 h-12 mr-4"
            style="border-radius: 50%"
          />
        </div>
        <div>Danh sách kết bạn</div>
      </router-link>
      <div
        class="-intro-y w-full h-16 dark-hover br-global flex items-center cursor-pointer transition duration-300 ease-in-out"
        v-ripple
      >
        <div class="pl-4">
          <img
            src="@/assets/img/group.png"
            class="w-12 h-12 mr-4"
            style="border-radius: 50%"
          />
        </div>
        <div>Danh sách nhóm</div>
      </div>
      <div
        class="-intro-y w-full h-10 dark-hover br-global flex items-center justify-between cursor-pointer transition duration-300 ease-in-out"
        v-ripple
        @click="isDown = !isDown"
      >
        <div class="ml-4">Bạn bè <span v-if="friends.length > 0">({{ friends.length }})</span> </div>
        <div>
          <v-btn
            icon
            :class="`mr-4 rotate ${!isDown || 'down'}`"
            color="#3a8df5"
          >
            <v-icon size="28">mdi-chevron-right</v-icon>
          </v-btn>
        </div>
      </div>
      <div v-show="isDown">
        <router-link
          v-for="user in friends"
          :key="user.id"
          tag="div"
          :to="{ name: 'y', params: { id: user.id } }"
          exact-active-class="f-active"
          class="w-full h-16 br-global dark-hover transition duration-300 ease-in-out"
          v-ripple
        >
          <Friend :user="user" />
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { request } from "@/api";
import { mapGetters } from "vuex";

export default {
  name: "SideContent",
  components: {
    Friend: () => import("./Friend.vue"),
  },
  data() {
    return {
      isDown: true,
      friends: [],
    };
  },
  computed: { ...mapGetters(["accessToken"]) },
  async created() {
    try {
      let { data } = await request("/api/friends?status=1", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${this.accessToken}`,
        },
      });
      if (data && data.length > 0) this.friends = data;
    } catch (error) {
      console.log(error.response);
    }
  },
};
</script>

<style>
.f-side-content {
  background-color: #232a3b;
  font-size: 1rem;
  line-height: 1.5rem;
}
.f-side-content:hover {
  overflow-y: overlay;
}
.f-side-content {
  overflow-y: hidden;
}

.f-side-content::-webkit-scrollbar {
  width: 8px;
}

.f-side-content::-webkit-scrollbar-track:hover {
  border-radius: 0.375rem;
  background-color: #7a7a7a;
}

.f-side-content::-webkit-scrollbar-thumb {
  border-radius: 0.375rem;
  background-color: #575757;
}
</style>