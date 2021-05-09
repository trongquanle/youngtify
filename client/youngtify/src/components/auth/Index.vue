<template>
  <div class="w-full h-screen p-5 md:p-20 flex items-center justify-center">
    <div class="intro-y auth">
      <img
        class="intro-y mx-auto w-16"
        alt="Youngtify"
        src="@/assets/img/logo_youngtify.svg"
      />
      <div class="intro-y auth__title text-2xl font-medium text-center mt-4">
        Youngtify
      </div>
      <router-view/>
    </div>
  </div>
</template>

<script>

export default {
  name: "Auth"
};
</script>

<style>
.border {
    /* border-width: 1px; */
    border: 1px solid #e2e8f0;
}
html.dark body *, html.dark body ::before, html.dark body ::after{
  border-color: unset;
}
</style>