<template>
  <div class="-intro-x w-4/5 pb-4 h-32 justify-self-center">
    <div
      class="w-full h-full flex items-center justify-between bc-cpn-global br-global"
    >
      <div class="h-full flex items-center">
        <v-avatar v-if="avatar" size="80" class="m-4">
          <img class="object-fit-cover" :src="avatar" />
        </v-avatar>
        <v-avatar v-else color="#4B5563" size="80" class="m-4">
          <span class="primary--text headline">{{ name }}</span>
        </v-avatar>
        <div>
          <div class="text-base">{{ user.fullname }}</div>
          <div class="text-sm text-gray-600 dark:text-gray-600">
            Muốn kết bạn
          </div>
          <div class="text-sm text-gray-600 dark:text-gray-600">
            "Xin chào, mình là {{ user.fullname }}. Kết bạn với mình nhé!"
          </div>
        </div>
      </div>
      <div class="h-full flex items-center justify-center gap-4">
        <!-- Chưa gửi lời mời -->
        <v-btn
          v-if="status == 0"
          @click="addFriend"
          :loading="isLoadding"
          color="primary"
        >
          <span class="normal-case text-dark">{{ textConfirm }}</span>
        </v-btn>
        <!-- Đã gửi lời mời -->
        <v-btn
          v-if="status == 1"
          @click="cancelRequest"
          :loading="isLoadding"
          text
          class="dark-hover"
          color="#e2e8f0"
        >
          <span class="normal-case">{{ textCancel }}</span>
        </v-btn>
        <v-btn v-if="status == 1" color="primary" :disabled="isDisabled">
          <span class="normal-case text-dark">{{ textConfirm }}</span>
        </v-btn>
        <!-- Bạn bè -->
        <v-btn v-if="status == 2" disabled>
          <span class="normal-case text-dark">{{ textConfirm }}</span>
        </v-btn>
        <!-- Danh sách lời mời -->
        <v-btn v-if="status == 4" disabled>
          <span class="normal-case text-dark">{{ textCancel }}</span>
        </v-btn>
        <v-btn
          v-if="status == 3"
          text
          class="dark-hover"
          @click="cancelAccept"
          color="#e2e8f0"
        >
          <span class="normal-case text-dark">{{ textCancel }}</span>
        </v-btn>
        <v-btn
          v-if="status == 3"
          @click="acceptFriend"
          :loading="isLoadding"
          color="primary"
          :disabled="isDisabled"
        >
          <span class="normal-case text-dark">{{ textConfirm }}</span>
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import { request } from "@/api";
import { mapGetters } from "vuex";
import { notifications } from "@/constant";

export default {
  name: "FriendCard",
  props: {
    action: {
      type: Number,
      default: 1,
    },
    user: {
      type: Object,
    },
  },
  data() {
    return {
      isLoadding: false,
      isDisabled: false,
      text: {
        ADD_FRIEND: "Thêm bạn bè",
        IGNORED: "Đã bỏ qua",
        ACCEPTED: "Đã đồng ý",
        ACCEPT: "Đồng ý",
        IGNORE: "Bỏ qua",
        CANCEL: "Hủy bỏ",
        SENDED: "Đã yêu cầu",
        FRIEND: "Bạn bè",
      },
      textCancel: undefined,
      textConfirm: undefined,
      status: undefined,
    };
  },
  created() {
    this.status = this.action;
    switch (this.status) {
      case 0:
        this.textConfirm = this.text.ADD_FRIEND;
        break;
      case 1:
        this.textCancel = this.text.CANCEL;
        this.textConfirm = this.text.SENDED;
        this.isDisabled = true;
        break;
      case 2:
        this.textConfirm = this.text.FRIEND;
        break;
      case 3:
        this.textCancel = this.text.IGNORE;
        this.textConfirm = this.text.ACCEPT;
        break;
      case 4:
        this.textCancel = this.text.IGNORE;
        this.textConfirm = this.text.ADD_FRIEND;
        break;
    }
  },
  computed: {
    ...mapGetters(["accessToken", "socket", "profile", "fullname"]),
    avatar() {
      if (this.user.avatar) {
        // console.log(this.user);
        if (this.user.avatar.search("https://") !== -1) return this.user.avatar;
        return `${process.env.VUE_APP_STORAGE_URL}${this.user.avatar}`;
      } else return undefined;
    },
    name() {
      try {
        const names = this.user.fullname.split(" ");
        return `${names[0][0]}${names[names.length - 1][0]}`;
      } catch (error) {
        return "YT";
      }
    },
  },
  methods: {
    addFriend: async function () {
      this.isLoadding = true;
      let objectAdd = {
        receiverId: this.user.id,
        status: 1,
      };
      try {
        let { data, status } = await request("/api/friends", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${this.accessToken}`,
          },
          data: objectAdd,
        });
        if (data && status == 200) {
          const notification = {
            postId: this.profile.id,
            userId: this.user.id,
            content: notifications.sendFriend,
            avataUrl: this.profile.avataUrl,
            fullName: this.fullname,
          };
          this.socket.emit("sendNotification", JSON.stringify(notification));
        }
      } catch (error) {
        console.log(error.response);
      } finally {
        this.status = 1;
        this.isLoadding = false;
        this.textCancel = this.text.CANCEL;
        this.textConfirm = this.text.SENDED;
        this.isDisabled = true;
      }
    },
    acceptFriend: async function () {
      this.isLoadding = true;
      let objectAdd = {
        senderId: this.user.id,
        status: 2,
      };
      try {
        let { data, status } = await request("/api/friends", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${this.accessToken}`,
          },
          data: objectAdd,
        });
        if (data && status == 200) {
          const notification = {
            postId: this.profile.id,
            userId: this.user.id,
            content: notifications.acceptFriend,
            avataUrl: this.profile.avataUrl,
            fullName: this.fullname,
          };
          this.socket.emit("sendNotification", JSON.stringify(notification));
        }
      } catch (error) {
        console.log(error.response);
      } finally {
        this.status = 2;
        this.isLoadding = false;
      }
    },
    cancelRequest: async function () {
      this.isLoadding = true;
      let objectAdd = {
        receiverId: this.user.id,
        status: 3,
      };
      try {
        let { data } = await request("/api/friends", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${this.accessToken}`,
          },
          data: objectAdd,
        });
      } catch (error) {
        console.log(error.response);
      } finally {
        this.status = 0;
        this.isLoadding = false;
        this.textConfirm = this.text.ADD_FRIEND;
      }
    },
    cancelAccept: async function () {
      this.isLoadding = true;
      let objectAdd = {
        senderId: this.user.id,
        status: 3,
      };
      try {
        let { data } = await request("/api/friends", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${this.accessToken}`,
          },
          data: objectAdd,
        });
      } catch (error) {
        console.log(error.response);
      } finally {
        this.status = 0;
        this.isLoadding = false;
        this.textConfirm = this.text.ADD_FRIEND;
      }
    },
  },
  watch: {
    status: function (newVal) {
      switch (newVal) {
        case 0:
          this.textConfirm = this.text.ADD_FRIEND;
          break;
        case 1:
          this.textCancel = this.text.CANCEL;
          this.textConfirm = this.text.SENDED;
          this.isDisabled = true;
          break;
        case 2:
          this.textConfirm = this.text.FRIEND;
          break;
        case 3:
          this.textCancel = this.text.IGNORE;
          this.textConfirm = this.text.ACCEPT;
          break;
        case 4:
          this.textCancel = this.text.IGNORE;
          this.textConfirm = this.text.ADD_FRIEND;
          break;
      }
    },
  },
};
</script>

<style>
</style>