<template>
  <div class="intro-x chat-top-bar w-full flex items-center px-4 py-3">
    <div class="flex items-center mr-auto">
      <Avatar
        :id="friendProfile.id"
        :title="fullname"
        :avatarUrl="friendProfile.avatarUrl"
      />
      <div class="ml-2 overflow-hidden">
        <a class="text-base font-medium">{{ fullname }}</a>
        <div class="chat-top-bar__sender-status">{{ atvText }}</div>
      </div>
    </div>
    <v-btn icon class="ml-2" @click="createRoom" color="#3a8df5">
      <svg width="34px" height="34px" viewBox="-5 -5 30 30">
        <path
          d="M10.952 14.044c.074.044.147.086.22.125a.842.842 0 001.161-.367c.096-.195.167-.185.337-.42.204-.283.552-.689.91-.772.341-.078.686-.105.92-.11.435-.01 1.118.174 1.926.648a15.9 15.9 0 011.713 1.147c.224.175.37.43.393.711.042.494-.034 1.318-.754 2.137-1.135 1.291-2.859 1.772-4.942 1.088a17.47 17.47 0 01-6.855-4.212 17.485 17.485 0 01-4.213-6.855c-.683-2.083-.202-3.808 1.09-4.942.818-.72 1.642-.796 2.136-.754.282.023.536.17.711.392.25.32.663.89 1.146 1.714.475.808.681 1.491.65 1.926-.024.31-.026.647-.112.921-.11.35-.488.705-.77.91-.236.17-.226.24-.42.336a.841.841 0 00-.368 1.161c.04.072.081.146.125.22a14.012 14.012 0 004.996 4.996z"
          fill="#3a8df5"
        ></path>
        <path
          d="M10.952 14.044c.074.044.147.086.22.125a.842.842 0 001.161-.367c.096-.195.167-.185.337-.42.204-.283.552-.689.91-.772.341-.078.686-.105.92-.11.435-.01 1.118.174 1.926.648.824.484 1.394.898 1.713 1.147.224.175.37.43.393.711.042.494-.034 1.318-.754 2.137-1.135 1.291-2.859 1.772-4.942 1.088a17.47 17.47 0 01-6.855-4.212 17.485 17.485 0 01-4.213-6.855c-.683-2.083-.202-3.808 1.09-4.942.818-.72 1.642-.796 2.136-.754.282.023.536.17.711.392.25.32.663.89 1.146 1.714.475.808.681 1.491.65 1.926-.024.31-.026.647-.112.921-.11.35-.488.705-.77.91-.236.17-.226.24-.42.336a.841.841 0 00-.368 1.161c.04.072.081.146.125.22a14.012 14.012 0 004.996 4.996z"
          stroke="#0084FF"
          fill="none"
        ></path>
      </svg>
    </v-btn>
    <v-btn icon class="ml-2" @click="createRoom" color="#3a8df5">
      <svg width="34px" height="34px" viewBox="-5 -5 30 30">
        <path
          d="M19.492 4.112a.972.972 0 00-1.01.063l-3.052 2.12a.998.998 0 00-.43.822v5.766a1 1 0 00.43.823l3.051 2.12a.978.978 0 001.011.063.936.936 0 00.508-.829V4.94a.936.936 0 00-.508-.828zM10.996 18A3.008 3.008 0 0014 14.996V5.004A3.008 3.008 0 0010.996 2H3.004A3.008 3.008 0 000 5.004v9.992A3.008 3.008 0 003.004 18h7.992z"
          fill="#3a8df5"
        ></path>
      </svg>
    </v-btn>
    <v-dialog v-model="dialog" max-width="400">
      <v-card>
        <v-card-title class="text-base text-dark font-bold">
          Lần chat video đến
        </v-card-title>
        <div class="w-full flex items-center justify-start px-6">
          <div>
            <v-avatar v-if="avatarCall" :size="56">
              <img class="object-fit-cover" :src="avatarCall" />
            </v-avatar>
            <v-avatar v-else color="#4B5563" :size="56">
              <span class="primary--text text-xl headline">{{ nameCall }}</span>
            </v-avatar>
          </div>
          <div class="text-dark ml-4">
            <a class="text-base font-medium"
              >{{ friendNameCall }} đang gọi cho bạn</a
            >
            <div class="text-gray-500">
              Cuộc gọi sẽ bắt đầu ngay khi bạn trả lời
            </div>
          </div>
        </div>
        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn text @click="cancelVideoCall">
            <span class="capitalize">Từ chối</span>
          </v-btn>

          <v-btn color="primary" @click="acceptVideoCall">
            <svg width="24" height="24" viewBox="-5 -5 30 30">
              <path
                d="M19.492 4.112a.972.972 0 00-1.01.063l-3.052 2.12a.998.998 0 00-.43.822v5.766a1 1 0 00.43.823l3.051 2.12a.978.978 0 001.011.063.936.936 0 00.508-.829V4.94a.936.936 0 00-.508-.828zM10.996 18A3.008 3.008 0 0014 14.996V5.004A3.008 3.008 0 0010.996 2H3.004A3.008 3.008 0 000 5.004v9.992A3.008 3.008 0 003.004 18h7.992z"
                fill="#fff"
              ></path>
            </svg>
            <span class="capitalize">Chấp nhận</span>
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-btn icon class="ml-2" color="#3a8df5" @click="showInfo">
      <v-icon size="28">mdi-information</v-icon>
    </v-btn>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import moment from "moment";
import { request } from "@/api";
import api from "@/api/stringeeApi";
import io from "socket.io-client";

export default {
  name: "TopBar",
  components: {
    Avatar: () => import("./../side_content/Avatar.vue"),
  },
  data() {
    return {
      dialog: false,
      userToken: "",
      roomToken: "",
      roomId: "",
      room: undefined,
      callClient: undefined,
      friendCall: undefined,
      timeout: undefined,
    };
  },
  computed: {
    ...mapGetters([
      "currentRoom",
      "friendProfile",
      "friendAtvs",
      "accessToken",
      "currentTime",
      "code",
      "socket",
      "friendAtvs",
      "profile",
    ]),
    fullname() {
      return `${this.friendProfile.lastName} ${this.friendProfile.firstName}`;
    },
    name() {
      if (this.friendProfile?.lastName && this.friendProfile?.firstName)
        return `${this.friendProfile.lastName[0].toUpperCase()}${this.friendProfile.firstName[0].toUpperCase()}`;
      else return undefined;
    },
    atvText() {
      if (this.friendAtvs.length > 0 && this.friendProfile) {
        let temp = this.friendAtvs.filter(
          (item) => item.id == this.friendProfile.id
        );
        if (temp.length > 0) {
          let atvText = "Đang hoạt động";
          if (!temp[0].status) {
            const duration = moment.duration(
              this.currentTime.diff(moment(temp[0]["time"]))
            );
            if (duration.asMinutes() < 60) {
              let minute = parseInt(duration.asMinutes());
              atvText = `Hoạt động ${minute == 0 ? 1 : minute} phút trước`;
            } else if (duration.asHours() < 24) {
              atvText = `Hoạt động ${parseInt(duration.asHours())} giờ trước`;
            } else if (duration.asDays() < 7) {
              atvText = `Hoạt động ${parseInt(duration.asDays())} ngày trước`;
            } else if (duration.asWeeks() < 4) {
              atvText = `Hoạt động ${parseInt(duration.asWeeks())} tuần trước`;
            } else if (duration.asMonths() < 12) {
              atvText = `Hoạt động ${parseInt(
                duration.asMonths()
              )} tháng trước`;
            } else {
              atvText = `Hoạt động ${parseInt(duration.asYears())} năm trước`;
            }
          }
          return atvText;
        }
      } else return undefined;
    },
    avatarCall() {
      const avt = this.friendCall?.avatarUrl;
      if (avt) {
        if (avt.search("https://") !== -1) return avt;
        else return `${process.env.VUE_APP_STORAGE_URL}${avt}`;
      } else return undefined;
    },
    friendNameCall() {
      if (this.friendCall)
        return `${this.friendCall.lastName} ${this.friendCall.firstName}`;
      else return "";
    },
    nameCall() {
      if (this.friendCall)
        return `${this.friendCall.lastName[0]} ${this.friendCall.firstName[1]}`;
      else return "";
    },
  },
  async created() {
    if (!this.friendProfile.id) {
      const userId = this.$route.params?.id;
      if (userId) {
        this.setFriendProfile({
          accessToken: this.accessToken,
          userId: userId,
        });
        try {
          let { data } = await request(`/api/conversations/${userId}`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${this.accessToken}`,
            },
          });
          if (data && data.roomId) {
            this.saveCurrentRoom(data.roomId);
            this.loadMessages();
          } else this.$router.push({ name: "youngtify" });
        } catch (error) {
          console.log(error);
        }
      }
    }
    this.socket.on("receiverVideoCall", (payload) => {
      this.roomId = payload?.roomId;
      this.timeout = setTimeout(() => {
        this.dialog = false;
        const sk = io(`${process.env.VUE_APP_REALTIME_URL}/videocall`);
        sk.emit("timeoutVideoCall", { roomId: this.roomId });
        setTimeout(() => {
          sk.disconnect();
        }, 2000);
      }, 60000);
      const userVideoCall = {
        senderId: payload.senderId,
        userId: payload.userId,
        roomId: this.roomId,
      };
      localStorage.setItem("userVideoCall", JSON.stringify(userVideoCall));
      if (this.roomId) {
        this.dialog = true;
        this.friendCall = payload;
      }
    });
  },
  methods: {
    ...mapActions(["setFriendProfile", "loadMessages", "saveCurrentRoom"]),
    showInfo() {
      this.$emit("showInfo");
    },
    getImage(url) {
      return `${process.env.VUE_APP_STORAGE_URL}${url}`;
    },
    createRoom: async function () {
      const friend = this.friendAtvs.find((f) => f.id == this.friendProfile.id);
      if (friend && friend.status == 1) {
        await api.setRestToken();
        const room = await api.createRoom();
        const { roomId } = room;
        this.roomId = roomId;
        this.socket.emit("videoCall", {
          roomId,
          senderId: this.code,
          userId: this.friendProfile.id,
          firstName: this.profile.firstName,
          lastName: this.profile.lastName,
          avatarUrl: this.profile.avatarUrl,
        });
        const userVideoCall = {
          senderId: this.code,
          userId: this.friendProfile.id,
          roomId,
        };
        localStorage.setItem("userVideoCall", JSON.stringify(userVideoCall));
        window.open(
          `http://localhost:8080/videocall?room=${roomId}`,
          "Video call - Youngtify",
          "height=720,width=1368"
        );
      } else {
        // TODO: Thông báo người dùng không hoạt động
      }
    },
    cancelVideoCall() {
      this.dialog = false;
      localStorage.removeItem("userVideoCall");
      clearTimeout(this.timeout);
      const sk = io(`${process.env.VUE_APP_REALTIME_URL}/videocall`);
      console.log(sk);
      sk.emit("rejectVideoCall", { roomId: this.roomId });
      setTimeout(() => {
        sk.disconnect();
      }, 2000);
    },
    acceptVideoCall() {
      this.dialog = false;
      clearTimeout(this.timeout);
      window.open(
        `http://localhost:8080/videocall?room=${this.roomId}`,
        "Video call - Youngtify",
        "height=720,width=1368"
      );
    },
  },
};
</script>

<style lang="scss"></style>
