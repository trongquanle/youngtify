<template>
  <div class="relative">
    <v-img
      class="cursor-pointer h-12 w-12 inline-block br-global"
      :src="image"
      rounded
    ></v-img>
    <div class="rm-image absolute">
      <v-btn class="btn-rm-image" icon color="#fff">
        <v-icon size="16">mdi-close</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  name: "ImageChat",
  props: {
    image: {
      type: String,
    },
  },
};
</script>

<style>
.btn-rm-image {
  box-shadow: 0 0 0 1px #0000001a;
  background-color: #3e4042;
  width: 24px !important;
  height: 24px !important;
}
.rm-image {
  top: -10px;
  right: -10px;
}
</style>
