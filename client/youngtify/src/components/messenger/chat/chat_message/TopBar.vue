<template>
  <div class="intro-x chat-top-bar w-full flex items-center px-4 py-3">
    <div class="flex items-center mr-auto">
      <v-badge bordered top color="green" dot offset-x="10" offset-y="10">
        <v-avatar size="48">
          <img class="object-fit-cover" src="@/assets/img/profile-7.jpg" />
        </v-avatar>
      </v-badge>
      <div class="ml-2 overflow-hidden">
        <a href="javascript:;" class="text-base font-medium">Robert De Niro</a>
        <div class="chat-top-bar__sender-status">Online</div>
      </div>
    </div>
    <v-btn icon class="ml-2" color="#3a8df5">
      <v-icon size="28">mdi-phone</v-icon>
    </v-btn>
    <v-btn icon class="ml-2" color="#3a8df5">
      <v-icon size="28">mdi-camera</v-icon>
    </v-btn>
    <v-btn icon class="ml-2" color="#3a8df5" @click="showInfo">
      <v-icon size="28">mdi-information</v-icon>
    </v-btn>
  </div>
</template>

<script>
export default {
  name: "TopBar",
  methods: {
    showInfo(){
      this.$emit("showInfo");
    }
  }
};
</script>

<style lang="scss">
</style>