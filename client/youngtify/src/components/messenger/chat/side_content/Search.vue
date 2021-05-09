<template>
  <div class="intro-y w-full h-6">
    <div class="relative flex justify-start items-center w-full h-10">
      <v-btn
        v-show="isFocusSearch"
        class="mr-1 tran-opacity"
        icon
        color="#e2e8f0"
      >
        <v-icon size="24">mdi-arrow-left</v-icon>
      </v-btn>
      <span v-show="!isFocusSearch" class="absolute y-search-icon tran-opacity">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 24 24"
          fill="#e2e8f0"
        >
          <path
            d="M23.809 21.646l-6.205-6.205c1.167-1.605 1.857-3.579 1.857-5.711 0-5.365-4.365-9.73-9.731-9.73-5.365 0-9.73 4.365-9.73 9.73 0 5.366 4.365 9.73 9.73 9.73 2.034 0 3.923-.627 5.487-1.698l6.238 6.238 2.354-2.354zm-20.955-11.916c0-3.792 3.085-6.877 6.877-6.877s6.877 3.085 6.877 6.877-3.085 6.877-6.877 6.877c-3.793 0-6.877-3.085-6.877-6.877z"
          /></svg
      ></span>
      <input
        ref="search"
        type="text"
        :style="width == 0 ? '' : `width: ${width}px`"
        :class="`tran-delay w-full y-input absolute ${
          isFocusSearch ? '' : 'pl-9'
        }`"
        placeholder="Nhập họ tên, email, số điện thoại..."
        @focus="isFocusSearch = true"
        @focusout="isFocusSearch = false"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: "Seach",
  data() {
    return {
      search: "",
      isFocusSearch: false,
      width: 0,
      oldWith: 0,
    };
  },
  mounted() {
    setTimeout(() => {
      let { width } = this.$refs["search"].getBoundingClientRect();
      this.width = width;
      this.oldWith = width;
    }, 1000);
  },
  watch: {
    isFocusSearch: function (newVal) {
      if (newVal) this.width -= 40;
      else this.width = this.oldWith;
    },
  },
};
</script>

<style>
</style>