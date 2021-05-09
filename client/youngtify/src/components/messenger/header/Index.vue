<template>
  <div
    class="-intro-y top-bar dark:bg-dark-2 dark:border-dark-2 top-0 left-0 w-full fixed h-16"
  >
    <div class="top-bar-content flex w-full h-full px-5">
      <a class="flex items-center h-full mr-auto" href="#">
        <img alt="Young Chat" class="h-8" src="@/assets/img/logo.svg" />
        <div class="text-base font-light ml-4">
          <span class="font-medium">Young</span> Messenger
        </div>
      </a>
      <div class="notification-dropdown dropdown relative">
        <a
          href="javascript:;"
          class="notification-dropdown__toggler dropdown-toggle h-full flex items-center px-5 relative -mr-3 md:mr-0 md:border-l md:border-r"
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
            class="feather feather-bell w-5 h-5"
          >
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
            <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
          </svg>
          <div
            class="notification-dropdown__toggler__count flex items-center justify-center absolute top-0 right-0 rounded-full"
          >
            2
          </div>
        </a>
      </div>

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
          >
            <a
              href="javascript:;"
              class="h-full dropdown-toggle flex items-center pl-5"
            >
              <v-avatar class="cursor-pointer" v-if="avatar" size="44">
                <v-img :src="avatar" :alt="fullname"></v-img>
              </v-avatar>
              <v-avatar v-else color="#4B5563" size="44">
                <span class="primary--text headline">{{ name }}</span>
              </v-avatar>
              <div class="hidden md:block ml-3">
                <div class="w-24 truncate text-base leading-tight">
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
          <v-list-item-group v-model="model" mandatory color="#3a8df5">
            <v-list-item
              class="y-list-item"
              v-for="(item, i) in items"
              :key="i"
            >
              <v-icon class="y-icon-item" v-text="item.icon"></v-icon>
              <v-list-item-title
                class="y-title-item"
                v-text="item.text"
              ></v-list-item-title>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-menu>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Header",
  data() {
    return {
      items: [
        {
          icon: "mdi-account-outline",
          text: "Profile",
        },
        {
          icon: "mdi-star",
          text: "Add account",
        },
        {
          icon: "mdi-send",
          text: "Reset password",
        },
        {
          icon: "mdi-logout ",
          text: "Logout",
        },
      ],
      model: 1,
    };
  },
  computed: { ...mapGetters(["avatar", "fullname", "name"]) },
};
</script>

<style>
</style>