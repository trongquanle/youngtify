<template>
  <v-app>
    <!-- <v-progress-linear
      indeterminate
      color="cyan"
    ></v-progress-linear> -->
    <!-- <keep-alive> -->
    <router-view />
    <!-- </keep-alive> -->
  </v-app>
</template>

<script>
import {mapActions} from 'vuex';

export default {
  name: "App",
  created(){
    this.initCurrentTime();
  },
  methods: {...mapActions(["initCurrentTime"])}
};
</script>
<style>
.theme--light.v-application {
  background: unset !important;
}
html {
  overflow-y: hidden !important;
}
.v-progress-linear{
  z-index: 100;
}
</style>
