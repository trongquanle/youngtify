<template>
  <div>
    <div class="clear-both mb-2"></div>
    <div
      :class="self ? 'float-right' : 'float-left'"
      class="chat-text-box__content flex items-center"
    >
      <MessageAction v-if="self" :self="self" />
      <!-- type text -->
      <div
        v-if="msg.messageType == 0"
        :class="!self || 'chat-text-box__content__text--primary'"
        class="chat-text-box__content__text px-3 py-2"
        v-html="msg.message"
      ></div>
      <!-- type image -->
      <div
        v-if="msg.messageType == 1"
        :class="`image-chat rounded-md flex flex-wrap justify-${self ? 'end' : 'start'}`"
      >
        <div v-for="img in images" :key="img" class="image-fit zoom-in">
          <v-img
            class="cursor-pointer h-24 w-24 br-global"
            :src="`http://localhost:3000${img}`"
          ></v-img>
        </div>
      </div>
      <MessageAction v-if="!self" :self="self" />
    </div>
  </div>
</template>

<script>
export default {
  name: "Message",
  props: {
    self: {
      type: Boolean,
    },
    msg: Object,
  },
  components: {
    MessageAction: () => import("./MessageAction"),
  },
  computed: {
    images() {
      if (this.msg.message) return this.msg.message.split(",");
      else return [];
    },
  },
};
</script>

<style>
.image-chat{
  grid-gap: 8px;
  max-width: 304px;
}
</style>
