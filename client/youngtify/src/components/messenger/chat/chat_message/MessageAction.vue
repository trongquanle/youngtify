<template>
  <div
    :class="self ? 'mr-1' : 'ml-1'"
    class="hidden sm:block dropdown relative"
  >
    <v-btn v-if="self" icon>
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
        class="feather feather-trash w-4 h-4"
      >
        <polyline points="3 6 5 6 21 6"></polyline>
        <path
          d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"
        ></path>
      </svg>
    </v-btn>
    <v-btn icon>
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
        class="feather feather-corner-up-left w-4 h-4"
      >
        <polyline points="9 14 4 9 9 4"></polyline>
        <path d="M20 20v-7a4 4 0 0 0-4-4H4"></path>
      </svg>
    </v-btn>
    <!-- <v-menu
      bottom
      origin="center center"
      :close-on-click="true"
      transition="scroll-y-transition"
      content-class="mt-10"
    >
      <template v-slot:activator="{ on, attrs }">
        <div
          v-bind="attrs"
          v-on="on"
          class="account-dropdown dropdown relative"
        >
          <v-btn icon color="#3a8df5">
            <v-icon size="24">mdi-dots-vertical </v-icon>
          </v-btn>
        </div>
      </template>
      <v-list rounded>
        <v-list-item-group v-model="model" mandatory color="#3a8df5">
          <v-list-item class="y-list-item" v-for="(item, i) in items" :key="i">
            <v-icon class="y-icon-item" v-text="item.icon"></v-icon>
            <v-list-item-title
              class="y-title-item"
              v-text="item.text"
            ></v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-menu> -->
  </div>
</template>

<script>
export default {
  name: "MessageAction",
  props: {
    self: {
      type: Boolean,
      default: false,
    },
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
    };
  },
};
</script>
