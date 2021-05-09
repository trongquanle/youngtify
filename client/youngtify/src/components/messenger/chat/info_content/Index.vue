<template>
  <div
    class="info-content h-full col-span-12 xl:col-span-3 flex flex-col overflow-hidden xl:pl-0 pr-4"
  >
    <div class="overflow-y-auto scrollbar-hidden py-4">
      <div class="intro-x w-full br-global bc-cpn-global p-4">
        <div class="w-full">
          <div class="w-20 h-20 mx-auto">
            <v-badge
              bordered
              bottom
              color="green accent-4"
              dot
              offset-x="15"
              offset-y="15"
            >
              <v-avatar v-if="friendProfile.avatar" size="80">
                <v-img :src="friendProfile.avatar"></v-img>
              </v-avatar>
              <v-avatar v-else color="#4B5563" size="80">
                <span class="primary--text headline font-bold">{{ name }}</span>
              </v-avatar>
            </v-badge>
          </div>
          <div class="text-base font-medium text-center mt-3 text-dark">
            {{ fullname }}
          </div>
          <div class="text-gray-600 text-center text-sm mt-0.5">
            Đang hoạt động
          </div>
        </div>
      </div>
      <div class="intro-y br-global text-dark bc-cpn-global mt-4">
        <div
          class="pl-4 pr-2 h-10 dark-hover br-global flex items-center justify-between cursor-pointer"
          v-ripple
          @click="isDown = !isDown"
        >
          <div class="text-base font-medium">
            Thông tin cá nhân
          </div>
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
            <v-icon size="24" color="#718096">
              mdi-cake-variant
            </v-icon>
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
            <v-icon size="24" color="#718096">
              mdi-email-outline
            </v-icon>
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
          class="pl-4 pr-2 h-10 dark-hover br-global flex items-center justify-between cursor-pointer"
          v-ripple
          @click="isShowImage = !isShowImage"
        >
          <div class="text-base font-medium">
            Ảnh
          </div>
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
              v-for="image in images"
              :key="image"
              class="cursor-pointer h-24 br-global image-fit zoom-in"
              :src="getImage(image)"
            ></v-img>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { request } from "@/api";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "InfoContent",
  data() {
    return {
      isDown: true,
      isShowImage: false,
      images: [],
    };
  },
  computed: {
    ...mapGetters(["accessToken", "currentRoom", "friendProfile"]),
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
  },
  methods: {
    ...mapActions(["setFriendProfile"]),
    getImage(url) {
      return `http://localhost:3000${url}`;
    },
  },
  watch: {
    async currentRoom(newVal) {
      if (this.isShowImage) {
        try {
          let { data } = await request(`/api/messages/${newVal}/images`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${this.accessToken}`,
            },
          });
          if (data && data.length > 0) {
            this.images = data;
          }
        } catch (error) {
          console.log(error.response);
        }
      }
    },
    async isShowImage(newVal) {
      if (newVal) {
        try {
          let { data } = await request(`/api/messages/${newVal}/images`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${this.accessToken}`,
            },
          });
          if (data && data.length > 0) {
            this.images = data;
          }
        } catch (error) {
          console.log(error.response);
        }
      }
    },
  },
};
</script>

<style>
.list-info-content {
  height: calc(100% - 300px);
  /* height: 350px; */
}
</style>
