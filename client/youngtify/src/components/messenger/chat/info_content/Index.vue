<template>
  <div
    class="info-content h-full col-span-12 xl:col-span-3 flex flex-col overflow-hidden xl:pl-0 pr-4"
  >
    <div class="overflow-y-auto scrollbar-hidden py-4">
      <div class="intro-x w-full br-global bc-cpn-global p-4">
        <div class="w-full">
          <div class="w-20 h-20 mx-auto">
            <Avatar
              :id="friendProfile.id"
              :title="fullname"
              :avatarUrl="friendProfile.avatarUrl"
              :size="80"
              :offset="15"
            />
          </div>
          <div class="text-base font-medium text-center mt-3 text-dark">
            {{ fullname }}
          </div>
          <div class="text-gray-600 text-center text-sm mt-0.5">
            {{ atvText }}
          </div>
        </div>
      </div>
      <div class="intro-y br-global text-dark bc-cpn-global mt-4">
        <div
          class="pl-4 pr-2 h-10 dark-hover br-global flex items-center justify-between cursor-pointer transition duration-300 ease-in-out"
          v-ripple
          @click="isDown = !isDown"
        >
          <div class="text-base font-medium">Thông tin cá nhân</div>
          <div>
            <v-btn icon :class="`rotate ${!isDown || 'down'}`" color="#3a8df5">
              <v-icon size="28">mdi-chevron-right</v-icon>
            </v-btn>
          </div>
        </div>
        <div v-show="isDown" class="-intro-y px-4 py-2">
          <div
            v-if="dateOfBirth"
            class="border-gray-200 dark:border-dark-5 flex items-center justify-between border-b pb-3"
          >
            <div>
              <div class="text-gray-600">Sinh nhật</div>
              <div class="mt-0.5">{{ dateOfBirth }}</div>
            </div>
            <v-icon size="24" color="#718096"> mdi-cake-variant </v-icon>
          </div>
          <div
            v-if="friendProfile.phoneNumber"
            class="border-gray-200 dark:border-dark-5 flex items-center border-b py-3"
          >
            <div>
              <div class="text-gray-600">Số điện thoại</div>
              <div class="mt-0.5">{{ friendProfile.phoneNumber }}</div>
            </div>
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
              class="feather feather-mic w-4 h-4 text-gray-600 ml-auto"
            >
              <path
                d="M12 1a3 3 0 0 0-3 3v8a3 3 0 0 0 6 0V4a3 3 0 0 0-3-3z"
              ></path>
              <path d="M19 10v2a7 7 0 0 1-14 0v-2"></path>
              <line x1="12" y1="19" x2="12" y2="23"></line>
              <line x1="8" y1="23" x2="16" y2="23"></line>
            </svg>
          </div>
          <div
            class="border-gray-200 dark:border-dark-5 flex items-center border-b justify-between py-3"
          >
            <div class="">
              <div class="text-gray-600">Email</div>
              <div class="mt-0.5">{{ friendProfile.email }}</div>
            </div>
            <v-icon size="24" color="#718096"> mdi-email-outline </v-icon>
          </div>

          <div
            v-if="gender"
            class="border-gray-200 dark:border-dark-5 flex items-center border-b justify-between py-3"
          >
            <div class="">
              <div class="text-gray-600">Giới tính</div>
              <div class="mt-0.5">{{ gender }}</div>
            </div>
            <v-icon size="24" color="#718096">
              {{ genderIcon }}
            </v-icon>
          </div>
        </div>
        <div
          class="pl-4 pr-2 h-10 dark-hover br-global flex items-center justify-between cursor-pointer transition duration-300 ease-in-out"
          v-ripple
          @click="isShowImage = !isShowImage"
        >
          <div class="text-base font-medium">Ảnh</div>
          <div>
            <v-btn
              icon
              :class="`rotate ${!isShowImage || 'down'}`"
              color="#3a8df5"
            >
              <v-icon size="28">mdi-chevron-right</v-icon>
            </v-btn>
          </div>
        </div>
        <div v-show="isShowImage" class="-intro-x px-4 pb-4 pt-1">
          <div class="w-full mt-2 grid gap-2 grid-cols-3">
            <v-img
              v-for="(image, i) in images"
              :key="image"
              @click="showSlideImages(i)"
              class="cursor-pointer h-24 br-global image-fit zoom-in"
              :src="`${VUE_APP_STORAGE_URL}${image}`"
            ></v-img>
          </div>
        </div>
      </div>
    </div>
    <vue-easy-lightbox
      escDisabled
      moveDisabled
      :visible="visible"
      :imgs="imgs"
      :index="index"
      @hide="handleHide"
    ></vue-easy-lightbox>
  </div>
</template>

<script>
import { request } from "@/api";
import { mapGetters, mapActions } from "vuex";
import moment from "moment";

export default {
  name: "InfoContent",
  components: {
    Avatar: () => import("./../side_content/Avatar.vue"),
  },
  data() {
    return {
      isDown: true,
      isShowImage: false,
      images: [],
      VUE_APP_STORAGE_URL: process.env.VUE_APP_STORAGE_URL,
      visible: false,
      index: 0,
    };
  },
  computed: {
    ...mapGetters([
      "accessToken",
      "currentRoom",
      "friendProfile",
      "friendAtvs",
      "currentTime",
    ]),
    dateOfBirth() {
      if (this.friendProfile.dateOfBirth) {
        let dob = this.friendProfile.dateOfBirth.substr(0, 10).split("-");
        return `${parseInt(dob[2])} tháng ${parseInt(dob[1])}, ${dob[0]}`;
      }
      return undefined;
    },
    gender() {
      if (this.friendProfile.gender)
        return this.friendProfile.gender == 1 ? "Nam" : "Nữ";
      else return undefined;
    },
    genderIcon() {
      if (this.friendProfile.gender)
        return this.friendProfile.gender == 1
          ? "mdi-gender-male"
          : "mdi-gender-female";
      else return undefined;
    },
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
    imgs(){
      if(this.images.length > 0){
        return this.images.map(img => `${process.env.VUE_APP_STORAGE_URL}${img}`);
      }else return [];
    }
  },
  methods: {
    ...mapActions(["setFriendProfile"]),
    handleHide() {
      this.visible = false;
    },
    showSlideImages(index) {
      this.index = index;
      this.visible = true;
    }
  },
  watch: {
    async currentRoom(newVal) {
      if (newVal && this.isShowImage) {
        try {
          let { data } = await request(`/api/messages/${newVal}/images`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${this.accessToken}`,
            },
          });
          // console.log(data);
          if (data && data.length > 0) {
            this.images = data;
          } else this.images = [];
        } catch (error) {
          console.log(error.response);
        }
      }
    },
    async isShowImage(newVal) {
      if (newVal) {
        try {
          let { data } = await request(
            `/api/messages/${this.currentRoom}/images`,
            {
              method: "GET",
              headers: {
                Authorization: `Bearer ${this.accessToken}`,
              },
            }
          );
          if (data && data.length > 0) {
            this.images = data;
          }
        } catch (error) {
          console.log(error.response);
        }
      }
    }
  },
};
</script>

<style>
.list-info-content {
  height: calc(100% - 300px);
  /* height: 350px; */
}
</style>
