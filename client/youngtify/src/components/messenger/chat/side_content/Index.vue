<template>
  <div class="sidebar-content h-full w-full pl-4 pt-4 pr-4">
    <Search />
    <ChatList />
  </div>
</template>

<script>
import Search from "./Search";
import ChatList from "./ChatList";

export default {
  name: "SideContent",
  components: {
    Search,
    ChatList
  },
};
</script>

<style lang="scss">
</style>