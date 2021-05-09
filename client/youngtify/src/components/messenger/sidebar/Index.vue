<template>
  <div class="side-menu hidden md:block top-0 left-0 fixed w-16 h-screen">
    <div
      class="side-menu__content -intro-x border-r w-full h-full pt-16 flex flex-col justify-center overflow-hidden"
    >
      <router-link
        tag="a"
        :to="{ name: 'youngtify' }"
        class="-intro-x side-menu__content__link dark-hover br-global relative tooltip py-5 tooltipstered"
        exact-active-class="side-menu__content__link--active f-active"
        v-ripple
      >
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
          class="feather feather-mail w-5 h-5 mx-auto"
        >
          <path
            d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"
          ></path>
          <polyline points="22,6 12,13 2,6"></polyline>
        </svg>
      </router-link>
      <router-link
        tag="a"
        :to="{ name: 'friends' }"
        class="-intro-x side-menu__content__link dark-hover br-global relative tooltip py-5 tooltipstered"
        active-class="side-menu__content__link--active f-active"
        v-ripple
      >
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
          class="feather feather-users w-5 h-5 mx-auto"
        >
          <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
          <circle cx="9" cy="7" r="4"></circle>
          <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
          <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
        </svg>
      </router-link>
      <a
        class="-intro-x side-menu__content__link relative tooltip py-5 tooltipstered"
        href="javascript:;"
        data-side="right"
        data-content="groups"
      >
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
          class="feather feather-edit w-5 h-5 mx-auto"
        >
          <path
            d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"
          ></path>
          <path
            d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"
          ></path>
        </svg>
      </a>
      <a
        class="-intro-x side-menu__content__link relative dark-hover br-global tooltip py-5 tooltipstered"
        @click="dialog = true"
        data-side="right"
        v-ripple
      >
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
          class="feather feather-user w-5 h-5 mx-auto"
        >
          <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
          <circle cx="12" cy="7" r="4"></circle>
        </svg>
      </a>
      <a
        class="-intro-x side-menu__content__link relative tooltip py-5 tooltipstered"
        href="login.html"
        data-side="right"
      >
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
          class="feather feather-lock w-5 h-5 mx-auto"
        >
          <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
          <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
        </svg>
      </a>
      <a
        class="-intro-x side-menu__content__link relative tooltip py-5 tooltipstered"
        href="register.html"
        data-side="right"
      >
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
          class="feather feather-user-plus w-5 h-5 mx-auto"
        >
          <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
          <circle cx="8.5" cy="7" r="4"></circle>
          <line x1="20" y1="8" x2="20" y2="14"></line>
          <line x1="23" y1="11" x2="17" y2="11"></line>
        </svg>
      </a>
      <a
        class="-intro-x side-menu__content__link relative dark-hover br-global tooltip py-5 tooltipstered"
        @click="dialog = true"
        data-side="right"
        v-ripple
      >
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
          class="feather feather-settings w-5 h-5 mx-auto"
        >
          <circle cx="12" cy="12" r="3"></circle>
          <path
            d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"
          ></path>
        </svg>
      </a>
    </div>
    <v-row justify="center">
      <v-dialog v-model="dialog" persistent max-width="400px">
        <v-card>
          <v-card-title>
            <span class="text-lg text-dark">Cập nhật thông tin</span>
          </v-card-title>
          <v-card-text>
            <div class="w-full h-52 relative">
              <img
                class="w-full h-42"
                src="https://cover.talk.zdn.vn/default"
                style="object-fit: cover"
              />
              <div
                class="w-full absolute flex items-center justify-center -bottom-0"
              >
                <v-avatar
                  class="cursor-pointer"
                  @click="openChooseFile"
                  v-if="avatar || avatarBase64"
                  size="80"
                >
                  <v-img :src="avatarImg" alt=""></v-img>
                </v-avatar>
                <v-avatar
                  class="cursor-pointer"
                  @click="openChooseFile"
                  v-else
                  color="#4B5563"
                  size="80"
                >
                  <span class="primary--text headline font-bold">{{
                    name
                  }}</span>
                </v-avatar>
                <input
                  class="hidden"
                  type="file"
                  ref="file"
                  @change="selectFile"
                />
              </div>
            </div>
            <div class="w-full mt-2 relative">
              <div class="w-full full">
                <div class="w-full h-6 flex items-center justify-center">
                  <span class="text-base">{{ fullname }}</span>
                </div>
              </div>
              <div class="absolute -top-0 -right-0">
                <v-btn class="btn-edit-name" color="primary" fab small dark>
                  <v-icon size="16">mdi-pencil</v-icon>
                </v-btn>
              </div>
              <v-tabs class="mt-4" v-model="tabs">
                <v-tab href="#mobile-tabs-5-2">
                  <v-icon>mdi-heart</v-icon
                  ><span class="text-medium ml-2">Tài khoản</span>
                </v-tab>
                <v-tab href="#mobile-tabs-5-1">
                  <v-icon>mdi-phone</v-icon>
                  <span class="text-medium ml-2">Liên hệ</span>
                </v-tab>
              </v-tabs>
              <v-tabs-items v-model="tabs">
                <v-tab-item :value="'mobile-tabs-5-2'">
                  <v-card flat>
                    <ProfileContact ref="contact" />
                  </v-card>
                </v-tab-item>
                <v-tab-item :value="'mobile-tabs-5-1'">
                  <v-card flat>
                    <ProfileInfo ref="info" />
                  </v-card>
                </v-tab-item>
              </v-tabs-items>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="dialog = false">
              Close
            </v-btn>
            <v-btn color="blue darken-1" text @click="getVal"> Save </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>

<script>
import { request } from "@/api";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "Sidebar",
  components: {
    ProfileInfo: () => import("./../setting/ProfileInfo.vue"),
    ProfileContact: () => import("./../setting/ProfileContact.vue"),
  },
  data: () => ({
    dialog: false,
    tabs: null,
    selectedFile: undefined,
    avatarBase64: undefined,
  }),
  computed: {
    ...mapGetters(["fullname", "name", "avatar", "accessToken"]),
    avatarImg() {
      if (this.avatarBase64) return this.avatarBase64;
      else return this.avatar;
    },
  },
  methods: {
    ...mapActions(["setProfile"]),
    async getVal() {
      let contact = this.$refs["contact"].getVal();
      let info = this.$refs["info"]?.getVal();
      let profile;
      let type = 1;
      if (info) profile = { ...contact, ...info };
      else {
        type = 2;
        profile = { ...contact };
      }
      let formData = new FormData();
      formData.append("file", this.selectedFile);
      formData.append("profile", JSON.stringify(profile));
      formData.append("type", type);
      try {
        let { data } = await request("/api/users", {
          method: "PUT",
          headers: {
            "Authorization": `Bearer ${this.accessToken}`,
            "Content-Type": "multipart/form-data",
          },
          data: formData,
        });
        this.setProfile();
      } catch (error) {
        console.log(error.response);
      }
    },
    openChooseFile() {
      this.$refs["file"].click();
    },
    async selectFile() {
      if (this.$refs.file.files.length > 0) {
        this.selectedFile = this.$refs.file.files[0];
        this.avatarBase64 = await this.toBase64(this.selectedFile);
      }
    },
    toBase64(file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
      });
    },
  },
};
</script>

<style lang="scss">
.theme--dark.v-card {
  background-color: #232a3b !important;
}
.theme--dark.v-sheet {
  background-color: #232a3b !important;
  border-color: unset !important;
  color: transparent !important;
}
.btn-edit-name {
  width: 24px !important;
  height: 24px !important;
}
.v-menu__content {
  background-color: unset !important;
}
.v-date-picker-header,
.v-date-picker-table {
  background-color: #232a3b;
}
.theme--dark.v-tabs > .v-tabs-bar {
  background-color: #232a3b !important;
}
.v-dialog > .v-card > .v-card__title {
  padding: 10px 24px 10px !important;
}
.v-dialog > .v-card > .v-card__text {
  padding: 0 24px 0 !important;
}
.v-dialog {
  overflow-y: hidden;
}
.v-dialog:hover {
  overflow-y: auto;
}

.v-dialog::-webkit-scrollbar {
  width: 8px;
}

.v-dialog::-webkit-scrollbar-track:hover {
  border-radius: 0.375rem;
  background-color: #7a7a7a;
}

.v-dialog::-webkit-scrollbar-thumb {
  border-radius: 0.375rem;
  background-color: #575757;
}
</style>