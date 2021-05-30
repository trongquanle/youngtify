<template>
  <div class="w-full h-screen pl-16 pt-16">
    <v-row class="h-full" no-gutters>
      <v-col class="h-full" cols="12" sm="3">
        <SideContent />
      </v-col>
      <v-col class="h-full" v-if="!rooms || rooms.length == 0" cols="12" sm="9">
        <div class="w-full h-full flex items-center justify-center bc-cpn-global">
          <div class="grid justify-items-stretch">
          <img width="350" class="justify-self-center" src="@/assets/img/inapp-welcome-screen-removebg-preview.png" >
          <div class="justify-self-center text-gray-500"> Bạn chưa có bất kì liên hệ nào </div>
          </div>
        </div>
      </v-col>
      <v-col class="h-full" v-else cols="12" :sm="isShowInfo ? 6 : 9">
        <ChatMessage @showInfo="showInfo" />
      </v-col>
      <v-col class="h-full" cols="12" :sm="3" v-show="isShowInfo">
        <InfoContent />
      </v-col>
    </v-row>
    <vue-easy-lightbox
      escDisabled
      moveDisabled
      :visible="visible"
      :imgs="imgs"
      :index="index"
      @hide="handleHide"
    ></vue-easy-lightbox>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';

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
  computed: {...mapGetters(["rooms", "imgs", "index"]),
    visible: {
      get(){
        return this.$store.getters.visible;
      },
      set(val){
        this.$store.dispatch("setVisible", val);
      }
    }
  },
  methods: {
    showInfo(){
      this.isShowInfo = !this.isShowInfo;
    },
    handleHide() {
      this.visible = false;
    },
  },
};
</script>

<style>
</style>