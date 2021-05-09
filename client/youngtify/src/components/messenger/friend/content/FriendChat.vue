<template>
  <v-row class="h-full" no-gutters>
      <v-col class="h-full" cols="12" :sm="isShowInfo ? 8 : 12">
        <ChatMessage @showInfo="showInfo" />
      </v-col>
      <v-col class="h-full" cols="12" :sm="4" v-show="isShowInfo">
        <InfoContent />
      </v-col>
  </v-row>
</template>

<script>
export default {
  name: "FriendChat",
  components: {
    ChatMessage: () => import("@/components/messenger/chat/chat_message/Index.vue"),
    InfoContent: () => import("@/components/messenger/chat/info_content/Index.vue"),
  },
  data(){
    return{
      isShowInfo: false
    }
  },
  methods: {
    showInfo() {
      this.isShowInfo = !this.isShowInfo;
    },
  },
};
</script>

<style>
</style>