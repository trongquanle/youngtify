<template>
  <div :class="self ? 'mr-1' : 'ml-1'" class="hidden sm:block dropdown relative">
    <v-menu
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
    </v-menu>
  </div>
</template>

<script>
export default {
  name: "MessageAction",
  props: {
    self: {
      type: Boolean,
      default: false,
    }
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