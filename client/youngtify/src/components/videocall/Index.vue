<template>
  <div class="w-screen h-screen relative bc-cpn-global">
    <div class="videocall-content w-full h-full absolute inset-x-0 top-0">
      <div class="w-full h-full flex items-center justify-center">
        <div class="relative">
          <div v-show="status != statusVideoCall.inCall">
            <div class="w-full flex justify-center">
              <v-avatar color="#4B5563" :size="100">
                <span class="primary--text text-xl headline">LT</span>
              </v-avatar>
            </div>
            <div class="w-full text-center text-xl font-bold mt-4">
              <span v-show="status == statusVideoCall.timeout">
                Không thể kết nối với
              </span>
              Lê Huy Tuân
              <span v-show="status == statusVideoCall.endCall">
                đã rời cuộc trò truyện
              </span>
              <span v-show="status == statusVideoCall.reject">
                đã từ chối tham gia cuộc trò chuyện
              </span>
            </div>
            <div
              v-show="status == statusVideoCall.waiting"
              class="w-full text-center text-gray-500 mt-4"
            >
              Đang kết nối...
            </div>
          </div>
          <div
            v-show="status == statusVideoCall.inCall"
            class="w-full flex items-center justify-start absolute"
            style="top: -68px"
          >
            <v-avatar color="#4B5563" :size="56">
              <span class="primary--text text-xl headline">LT</span>
            </v-avatar>
            <div class="text-base font-bold ml-2">Lê Huy Tuân</div>
          </div>
          <div v-show="status == statusVideoCall.inCall" id="videos"></div>
        </div>
      </div>
    </div>
    <div class="videocall-controll w-full py-3 absolute bottom-0">
      <div class="w-full h-full flex items-end justify-center relative">
        <div class="group-control pb-4">
          <v-tooltip top>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                fab
                class="ml-2"
                v-bind="attrs"
                v-on="on"
                @click="onCloseTab"
              >
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </template>
            <span>Đóng cửa sổ</span>
          </v-tooltip>
          <v-tooltip top v-if="status == statusVideoCall.inCall">
            <template v-slot:activator="{ on, attrs }">
              <v-btn fab color="#ff4242" class="ml-2" v-bind="attrs" v-on="on">
                <svg viewBox="0 0 48 48" width="48" height="48">
                  <path
                    fill="#fff"
                    d="M12.054 24.58C12.52 19.102 24.34 19.002 24.54 19c.202 0 12.024-.1 12.408 5.37.284 4.048-1.092 3.74-1.56 3.778-.41.033-4.823-.767-5.452-1.19-.63-.422-.785-2.703-1.293-3.094-.507-.39-3.092-.51-4.134-.502-1.043.01-3.63.174-4.143.572-.513.4-.702 2.683-1.337 3.117-.636.434-5.062 1.31-5.47 1.283-.47-.03-1.85.3-1.506-3.753"
                  ></path>
                </svg>
              </v-btn>
            </template>
            <span>Dừng cuộc trò truyện</span>
          </v-tooltip>
        </div>
        <div
          id="self-videos"
          class="videocall-self absolute w-60 bottom-0"
        ></div>
      </div>
    </div>
  </div>
</template>

<script>
import { tokenDecode } from "@/util/tokenUtil";
import api from "@/api/stringeeApi";
import io from "socket.io-client";
import { statusVideoCall } from "@/constant";

export default {
  name: "VideoCall",
  data() {
    return {
      code: undefined,
      dialog: false,
      userToken: "",
      roomToken: "",
      roomId: "",
      room: undefined,
      callClient: undefined,
      self: true,
      status: true,
      socket: undefined,
      statusVideoCall: statusVideoCall,
    };
  },
  created() {
    const id = tokenDecode()?.id;
    if (id) this.code = id;
    else {
      this.$router.push({ name: "login" });
      return;
    }
    this.socket = io(`${process.env.VUE_APP_REALTIME_URL}/videocall`);
    this.socket.on("onFinishVideoCall", () => {
      console.log('end videocall');
      this.status = this.statusVideoCall.endCall;
    });
    this.socket.on("rejectVideoCall", () => {
      this.status = this.statusVideoCall.reject;
    });
    this.socket.on("timeoutVideoCall", () => {
      this.status = this.statusVideoCall.timeout;
    });
    const userVideoCall = JSON.parse(localStorage.getItem("userVideoCall"));
    this.status = this.statusVideoCall.waiting;
    if (userVideoCall) {
      if (id == userVideoCall.senderId)
        this.socket.emit("createRoomVideoCall", userVideoCall);
      else if (id == userVideoCall.userId)
        this.socket.emit("userConnectRoom", { roomId: userVideoCall.roomId });
      else this.$router.push({ name: "login" });
    }
    window.onbeforeunload = function () {
      localStorage.removeItem("userVideoCall");
    };
  },
  async mounted() {
    const urlParams = new URLSearchParams(location.search);
    console.log(urlParams);
    const roomId = urlParams.get("room");
    if (roomId) {
      this.roomId = roomId;
      await this.join();
    }
  },
  // destroyed() {
  //   this.socket.emit("finishVideoCall", {
  //     roomId: this.roomId,
  //     userId: this.userId,
  //   });
  //   this.socket.disconnect();
  //   localStorage.removeItem("userVideoCall");
  // },
  methods: {
    onCloseTab() {
      // this.socket.emit("finishVideoCall", {
      //   roomId: this.roomId,
      //   userId: this.userId,
      // });
      window.close();
    },
    authen: function () {
      return new Promise(async (resolve) => {
        const userToken = await api.getUserToken(this.code);
        api.restToken = userToken;
        this.userToken = userToken;

        if (!this.callClient) {
          const client = new StringeeClient();

          client.on("authen", function (res) {
            console.log("on authen: ", res);
            resolve(res);
          });
          this.callClient = client;
        }
        this.callClient.connect(userToken);
      });
    },
    publish: async function () {
      const localTrack = await StringeeVideo.createLocalVideoTrack(
        this.callClient,
        {
          audio: true,
          video: true,
          screen: false,
          videoDimensions: { width: 640, height: 360 },
        }
      );
      console.log(localTrack);
      const videoElement = localTrack.attach();
      this.addVideo(videoElement);

      const roomData = await StringeeVideo.joinRoom(
        this.callClient,
        this.roomToken
      );
      const room = roomData.room;
      console.log({ roomData, room });

      if (!this.room) {
        this.room = room;
        room.clearAllOnMethos();
        room.on("addtrack", (e) => {
          const track = e.info.track;

          console.log("addtrack", track);
          if (track.serverId === localTrack.serverId) {
            console.log("local");
            return;
          }
          this.subscribe(track);
        });
        room.on("removetrack", (e) => {
          const track = e.track;
          if (!track) {
            return;
          }

          const mediaElements = track.detach();
          mediaElements.forEach((element) => element.remove());
        });

        // Join existing tracks
        roomData.listTracksInfo.forEach((info) => this.subscribe(info));
      }

      await room.publish(localTrack);
      console.log("room publish successful");
    },
    createRoom: async function () {
      this.clearRoom();
      const friend = this.friendAtvs.find((f) => f.id == this.friendProfile.id);
      if (friend) {
        this.dialog = true;
        await api.setRestToken();
        const room = await api.createRoom();
        const { roomId } = room;
        const roomToken = await api.getRoomToken(roomId);

        this.roomId = roomId;
        this.roomToken = roomToken;
        console.log({ roomId, roomToken });

        await this.authen();
        await this.publish();
        this.socket.emit("videoCall", {
          roomId,
          userId: this.friendProfile.id,
          firstName: this.profile.firstName,
          lastName: this.profile.lastName,
          avatarUrl: this.profile.avatarUrl,
        });
      } else {
        // TODO: Thông báo người dùng không hoạt động
      }
    },
    join: async function () {
      await api.setRestToken();
      const roomToken = await api.getRoomToken(this.roomId);
      this.roomToken = roomToken;

      await this.authen();
      await this.publish();
    },
    joinWithId: async function () {
      const roomId = prompt("Dán Room ID vào đây nhé!");
      if (roomId) {
        this.roomId = roomId;
        await this.join();
      }
    },
    clearRoom() {
      this.userToken = "";
      this.roomId = "";
      this.roomToken = "";
      this.room = undefined;
      this.callClient = undefined;
    },
    subscribe: async function (trackInfo) {
      const track = await this.room.subscribe(trackInfo.serverId);
      track.on("ready", () => {
        const videoElement = track.attach();
        this.addVideo(videoElement);
      });
    },
    addVideo: function (video) {
      let videoContainer;
      if (this.self) {
        videoContainer = document.querySelector("#self-videos");
        this.self = false;
      } else {
        this.status = this.statusVideoCall.inCall;
        videoContainer = document.querySelector("#videos");
      }
      video.setAttribute("controls", "true");
      video.setAttribute("playsinline", "true");
      videoContainer.appendChild(video);
    },
  },
};
</script>

<style>
.h-56 {
  height: 14rem;
}
.videocall-self {
  width: 332px;
  right: 12px;
}
button {
  outline: none !important;
}

.bc-cpn-global {
  background-color: #232a3b;
}
</style>