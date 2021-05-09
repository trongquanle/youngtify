<template>
  <div class="chat-box h-full w-full pt-4 pr-4">
    <TopBar @showInfo="showInfo" />
    <ChatContent v-on:showInfo="showInfo" />
    <!-- <ChatInput /> -->
    <ChatDiv />
  </div>
</template>

<script>
import TopBar from "./TopBar";
import ChatContent from "./ChatContent";
import ChatInput from "./ChatInput";
import ChatDiv from "./ChatDiv";

export default {
  name: "ChatMesage",
  components: {
    TopBar,
    ChatContent,
    ChatInput,
    ChatDiv
  },
  methods: {
    showInfo(){
      this.$emit("showInfo");
    }
  }
};
</script>

<style lang="scss">
</style>