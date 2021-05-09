<template>
  <div @click="onJoinRoom">
    <v-badge
      bordered
      bottom
      color="white-black"
      dot
      offset-x="10"
      offset-y="10"
    >
      <v-avatar size="40">
        <img
          class="object-fit-cover"
          :src="`http://localhost:3000${room.avatarUrl}`"
        />
      </v-avatar>
    </v-badge>
    <div class="ml-2 overflow-hidden">
      <a href="javascript:;" class="chat-list__name font-medium">{{
        room.title
      }}</a>
      <div class="flex items-center text-xs">
        <div class="chat-list__text w-3/5 truncate">
          {{ room.messageTitle }}
        </div>
        <div class="chat-list__time whitespace-no-wrap ml-2">01:10 PM</div>
      </div>
    </div>
    <div class="dropdown absolute top-0 right-0 mt-1 mr-4 ml-auto">
      <a class="dropdown-toggle w-5 h-5 block" href="javascript:;">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="1"
          stroke-linecap="round"
          stroke-linejoin="round"
          class="feather feather-more-horizontal w-6 h-6"
        >
          <circle cx="12" cy="12" r="1"></circle>
          <circle cx="19" cy="12" r="1"></circle>
          <circle cx="5" cy="12" r="1"></circle>
        </svg>
      </a>
    </div>
  </div>
</template>

<script>
export default {
    name: 'RoomItem',
    props: {
        room: {
            type: Object,
            required: true
        }
    },
    methods: {
        onJoinRoom(){
            console.log(this.room);
            this.$emit('joinRoom', this.room.id);
        }
    }
};
</script>

<style>
</style>