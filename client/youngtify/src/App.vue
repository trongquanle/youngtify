<template>
  <v-app>
    <keep-alive>
      <router-view />
    </keep-alive>
  </v-app>
</template>

<script>
export default {
  name: "App",
};
</script>
<style>
.theme--light.v-application {
  background: unset !important;
}
html {
  overflow-y: hidden !important;
}
</style>
