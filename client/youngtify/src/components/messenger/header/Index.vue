<template>
  <div
    class="-intro-y top-bar dark:bg-dark-2 dark:border-dark-2 top-0 left-0 w-full fixed h-16"
  >
    <div class="top-bar-content flex w-full h-full">
      <a class="flex items-center h-full mr-auto pl-5">
        <img alt="Young Chat" class="h-8" src="@/assets/img/logo.svg" />
        <div class="text-base font-light ml-4">
          <span class="font-medium">Youngtify</span>
        </div>
      </a>
      <Notification />
      <v-menu
        bottom
        origin="center center"
        :close-on-click="true"
        transition="scroll-y-transition"
        content-class="mt-14"
      >
        <template v-slot:activator="{ on, attrs }">
          <div
            v-bind="attrs"
            v-on="on"
            class="account-dropdown dropdown relative"
            v-ripple
          >
            <a class="h-full dropdown-toggle flex items-center px-5">
              <v-avatar class="cursor-pointer" v-if="avatar" size="44">
                <v-img :src="avatar" :alt="fullname"></v-img>
              </v-avatar>
              <v-avatar v-else color="#4B5563" size="44">
                <span class="primary--text headline">{{ name }}</span>
              </v-avatar>
              <div class="hidden md:block ml-3">
                <div class="text-base leading-tight">
                  {{ fullname }}
                </div>
                <!-- <div class="account-dropdown__info uppercase">
                  Frontend Engineer
                </div> -->
              </div>
            </a>
          </div>
        </template>
        <v-list rounded>
          <v-list-item-group
            class="y-list"
            v-model="model"
            mandatory
            color="#3a8df5"
          >
            <v-list-item
              class="y-list-item transition duration-300 ease-in-out"
            >
              <div>
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
                  class="feather feather-user w-5 h-5 mr-2"
                >
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
              </div>
              <v-list-item-title
                class="y-title-item"
                v-text="`Thông tin cá nhân`"
              ></v-list-item-title>
            </v-list-item>

            <v-list-item
              class="y-list-item transition duration-300 ease-in-out"
            >
              <div>
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
                  class="feather feather-lock w-5 h-5 mr-2"
                >
                  <rect
                    x="3"
                    y="11"
                    width="18"
                    height="11"
                    rx="2"
                    ry="2"
                  ></rect>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                </svg>
              </div>
              <v-list-item-title
                class="y-title-item"
                v-text="`Đặt lại mật khẩu`"
              ></v-list-item-title>
            </v-list-item>

            <v-list-item
              class="y-list-item transition duration-300 ease-in-out"
              @click="logout"
            >
              <div>
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
                  class="feather feather-toggle-right w-5 h-5 mr-2"
                >
                  <rect x="1" y="5" width="22" height="14" rx="7" ry="7"></rect>
                  <circle cx="16" cy="12" r="3"></circle>
                </svg>
              </div>
              <v-list-item-title
                class="y-title-item"
                v-text="`Đăng xuất`"
              ></v-list-item-title>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-menu>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  name: "Header",
  data() {
    return {
      model: 0,
      showNotification: false,
      isClickOuside: false,
    };
  },
  components: {
    Notification: () => import("./Notification.vue"),
  },
  computed: { ...mapGetters(["avatar", "fullname", "name", "socket"]) },
  methods: {
    ...mapActions(["resetAuth", "resetSocket"]),
    logout() {
      this.socket.disconnect();
      localStorage.clear();
      this.resetAuth();
      this.resetSocket();
      this.$router.push({name: "login"});
      // window.location.href = "auth/login";
    },
    onShowNotification() {
      this.showNotification = !this.showNotification;
    },
    onHideNotification() {
      if (this.isClickOuside) {
        this.showNotification = false;
        this.isClickOuside = false;
      }
    },
  },
  watch: {
    showNotification: function (newVal) {
      if (newVal) {
        setTimeout(() => {
          this.isClickOuside = true;
        }, 0);
      } else this.isClickOuside = false;
    },
  },
};
</script>

<style></style>
