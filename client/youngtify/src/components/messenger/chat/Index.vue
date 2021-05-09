<template>
  <div class="w-full h-screen pl-16 pt-16">
    <v-row class="h-full" no-gutters>
      <v-col class="h-full" cols="12" sm="3">
        <SideContent />
      </v-col>
      <v-col class="h-full" cols="12" :sm="isShowInfo ? 6 : 9">
        <ChatMessage @showInfo="showInfo" />
      </v-col>
      <v-col class="h-full" cols="12" :sm="3" v-show="isShowInfo">
        <InfoContent />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
export default {
  name: "Chat",
  components: {
    SideContent: () => import("./side_content/Index"),
    ChatMessage: () => import("./chat_message/Index"),
    InfoContent: () => import("./info_content/Index"),
  },
  data(){
    return{
      isShowInfo: false
    }
  },
  created() {
    let { id } = this.profile;
    this.initSocketEvents(id);
  },
  computed: { ...mapGetters(["profile"]) },
  methods: { ...mapActions(["initSocketEvents"]),
    showInfo(){
      this.isShowInfo = !this.isShowInfo;
    }
  },
};
</script>

<style>
</style>