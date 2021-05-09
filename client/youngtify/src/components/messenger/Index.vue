<template>
  <div>
    <Header />
    <Sidebar />
    <keep-alive>
      <router-view />
    </keep-alive>
  </div>
</template>

<script>
import {mapActions} from 'vuex';

import Header from "./header/Index";
import Sidebar from "./sidebar/Index";

export default {
  name: "Messenger",
  components: {
    Header,
    Sidebar,
    Chat: () => import("./chat/Index.vue"),
  },
  created(){
    this.setProfile();
  },
  methods: {...mapActions(["setProfile"])}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
@import url("./../../assets/css/pages/messenger/index.css");
.v-list--rounded .v-list-item,
.v-list--rounded .v-list-item:before,
.v-list--rounded .v-list-item > .v-ripple__container {
  border-radius: 4px !important;
}
.v-list-item--link:before {
  background-color: unset;
}
// @import "@/styles/custom.scss";
</style>
