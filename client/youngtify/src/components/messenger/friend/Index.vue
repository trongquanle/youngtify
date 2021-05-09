<template>
  <div class="w-full h-screen pl-16 pt-16">
    <v-row class="h-full" no-gutters>
      <v-col class="h-full" cols="12" sm="3">
        <SideContent />
      </v-col>
      <v-col class="h-full" cols="12" sm="9">
        <Content />
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  name: "Friend",
  components: {
    SideContent: () => import("./side_content/Index.vue"),
    Content: () => import("./content/Index.vue"),
  }
};
</script>

<style></style>
