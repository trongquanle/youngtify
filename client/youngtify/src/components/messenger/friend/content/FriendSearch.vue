<template>
  <div class="h-full w-full pt-4 pb-4 pr-4 text-dark">
    <div
      class="y-max-width-content w-full y-scrollbar grid justify-items-stretch"
    >
      <div class="-intro-x w-4/5 pb-4 justify-self-center text-base font-bold">
        Tìm kiếm bạn bè
      </div>
      <div class="-intro-x w-4/5 pb-14 h-10 justify-self-center">
        <div class="relative flex justify-start items-center w-3/5 h-10">
          <v-btn
            v-show="isFocusSearch"
            class="mr-1 tran-opacity"
            icon
            color="#e2e8f0"
          >
            <v-icon size="24">mdi-arrow-left</v-icon>
          </v-btn>
          <span
            v-show="!isFocusSearch"
            class="absolute y-search-icon tran-opacity"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="#e2e8f0"
            >
              <path
                d="M23.809 21.646l-6.205-6.205c1.167-1.605 1.857-3.579 1.857-5.711 0-5.365-4.365-9.73-9.731-9.73-5.365 0-9.73 4.365-9.73 9.73 0 5.366 4.365 9.73 9.73 9.73 2.034 0 3.923-.627 5.487-1.698l6.238 6.238 2.354-2.354zm-20.955-11.916c0-3.792 3.085-6.877 6.877-6.877s6.877 3.085 6.877 6.877-3.085 6.877-6.877 6.877c-3.793 0-6.877-3.085-6.877-6.877z"
              /></svg
          ></span>
          <input
            ref="search"
            type="text"
            :style="width == 0 ? '' : `width: ${width}px`"
            :class="`tran-delay w-full y-input absolute ${
              isFocusSearch ? '' : 'pl-9'
            }`"
            placeholder="Nhập họ tên, email, số điện thoại..."
            v-model="search"
            @focus="isFocusSearch = true"
            @focusout="isFocusSearch = false"
            @keyup="stackT"
          />
        </div>
      </div>
      <FriendCard
        v-for="user in friendSearch"
        :user="user"
        :key="user.id"
        :action="user.status"
      />
      <FriendSkeleton v-for="i in 3" :key="i" v-show="isLoadding" />
    </div>
    <div v-show="friendSearch.length == 0 && !isLoadding" class="empty-search intro-x flex items-center justify-center w-full">
      <div class="grid justify-items-stretch">
        <img
          class="justify-self-center"
          src="@/assets/img/empty-search.png"
          width="160"
          height="130"
          alt="empty-search.png"
        />
        <div class="justify-self-center mt-4 text-gray-500">
          Không tìm thấy kết quả
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { request } from "@/api";

export default {
  name: "FriendSearch",
  components: {
    FriendCard: () => import("./FriendCard.vue"),
    FriendSkeleton: () => import("./FriendSkeleton.vue"),
  },
  data() {
    return {
      search: "",
      isFocusSearch: false,
      width: 0,
      oldWith: 0,
      friendSearch: [],
      isLoadding: false,
      stackSearch: undefined,
    };
  },
  computed: { ...mapGetters(["accessToken"]) },
  mounted() {
    setTimeout(() => {
      let { width } = this.$refs["search"].getBoundingClientRect();
      this.width = width;
      this.oldWith = width;
      this.$refs["search"].focus();
    }, 100);
  },
  methods: {
    stackT: function () {
      let valSearch = this.search.trim();
      if (valSearch) {
        this.isLoadding = true;
        this.friendSearch = [];
        if (this.stackSearch) clearTimeout(this.stackSearch);
        this.stackSearch = setTimeout(this.searchFriend, 1000);
      } else {
        this.isLoadding = false;
        this.friendSearch = [];
      }
    },
    searchFriend: async function () {
      let valSearch = this.search.trim();
      if (valSearch) {
        try {
          let { data } = await request(`/api/search?key=${valSearch}`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${this.accessToken}`,
            },
          });
          this.friendSearch = data;
        } catch (error) {
          console.log(error.response);
        } finally {
          this.isLoadding = false;
        }
      }
    },
  },
  watch: {
    isFocusSearch: function (newVal) {
      if (newVal) this.width -= 40;
      else this.width = this.oldWith;
    },
  },
};
</script>

<style>
.empty-search{
  height: calc(100% - 96px);
}
</style>