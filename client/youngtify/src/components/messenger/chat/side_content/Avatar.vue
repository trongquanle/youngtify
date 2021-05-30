<template>
  <v-badge
    bordered
    bottom
    :color="color"
    dot
    :offset-x="offset"
    :offset-y="offset"
  >
    <v-avatar v-if="avatar" :size="size">
      <img class="object-fit-cover" :src="avatar" />
    </v-avatar>
    <v-avatar v-else color="#4B5563" :size="size">
      <span class="primary--text headline">{{ name }}</span>
    </v-avatar>
  </v-badge>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "Avatar",
  props: {
    id: String,
    title: String,
    avatarUrl: String,
    size: {
      type: Number,
      default: 48,
      required: false,
    },
    offset: {
      type: Number,
      default: 10,
      required: false,
    },
  },
  computed: {
    ...mapGetters(["friendAtvs"]),
    status() {
      if (this.friendAtvs.length) {
        let temp = this.friendAtvs.filter((item) => item.id == this.id);
        if (temp.length > 0) {
          return temp[0].status;
        }
      } else return undefined;
    },
    name() {
      let names = this.title.split(" ");
      return `${names[0][0]}${names[names.length - 1][0]}`;
    },
    avatar() {
      if (this.avatarUrl) {
        if (this.avatarUrl.search("https://") !== -1) return this.avatarUrl;
        return `${process.env.VUE_APP_STORAGE_URL}${this.avatarUrl}`;
      }
      return undefined;
    },
    color() {
      return this.status ? "green accent-4" : "white-black";
    },
  },
  methods: {},
};
</script>

<style></style>
