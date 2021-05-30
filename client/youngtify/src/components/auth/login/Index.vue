<template>
  <div class="intro-y box px-5 py-8 mt-4" style="opacity: 0.9 !important">
    <div class="intro-y">
      <v-form ref="form" v-model="valid" lazy-validation>
        <v-text-field
          v-model="email"
          label="Email"
          autofocus
          placeholder="Nhập email"
          ref="email"
          :rules="rules.emailRules"
          :error="validate.email.invalid"
          :error-messages="validate.email.msg"
          @blur="clearError('email')"
          required
          outlined
          prepend-inner-icon="mdi-email"
        ></v-text-field>
        <v-text-field
          v-model="password"
          @keyup.enter.prevent="onLogin"
          label="Mật khẩu"
          :type="isShowPwd ? 'text' : 'password'"
          placeholder="Nhập mật khẩu"
          outlined
          ref="pwd"
          @blur="clearError('pwd')"
          :append-icon="isShowPwd ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="isShowPwd = !isShowPwd"
          :rules="rules.pwdRules"
          :error="validate.pwd.invalid"
          :error-messages="validate.pwd.msg"
          prepend-inner-icon="mdi-lock"
        ></v-text-field>
      </v-form>
    </div>
    <div class="intro-y auth__info flex text-xs sm:text-sm justify-end">
      <a>Quên mật khẩu?</a>
    </div>
    <div class="intro-y mt-5 xl:mt-8 text-center xl:text-left">
      <v-btn
        class="intro-y button--primary w-full outline-none color-white"
        :loading="loading"
        :disabled="loading"
        color="#3A8DF5"
        large
        @click="onLogin"
      >
        Login
      </v-btn>
      <div class="py-4 text-gray-400">Hoặc đăng nhập với</div>
      <div
        class="w-full flex items-center justify-center"
        style="grid-gap: 16px"
      >
        <v-btn
          class="outline-none"
          @click="$refs['googleButton'].handleClick()"
          small
          outlined
          fab
          color="#e0ebf7"
        >
          <v-icon size="24">mdi-google</v-icon>
        </v-btn>
        <v-btn
          class="outline-none"
          @click="onLoginFacebook"
          small
          outlined
          fab
          color="#e0ebf7"
        >
          <v-icon size="24">mdi-facebook</v-icon>
        </v-btn>
        <GoogleLogin
          :ref="`googleButton`"
          class="hidden"
          :params="params"
          :onSuccess="onSuccess"
          :onFailure="onFailure"
          v-ripple
        >
          Google
        </GoogleLogin>
        <GoogleLogin
          ref="googleButton"
          class="hidden"
          :params="params"
          :onSuccess="onSuccess"
          :onFailure="onFailure"
          v-ripple
        >
          Facebook
        </GoogleLogin>
        <facebook-login
          :ref="`facebookButton`"
          class="hidden"
          appId="467771157626231"
          @login="getUserData"
          @logout="onLogout"
        >
        </facebook-login>
      </div>
      <!-- <button
        class="intro-y button button--lg button--secondary w-full border mt-3"
      >
        Sign up
      </button> -->
    </div>
  </div>
</template>

<script>
import { GoogleLogin } from "vue-google-login";
import facebookLogin from "facebook-login-vuejs";
import { mapActions } from "vuex";
import axios from "axios";
import { request } from "@/api";

export default {
  name: "Login",
  components: {
    GoogleLogin,
    facebookLogin,
  },
  data: () => {
    return {
      params: {
        client_id: process.env.VUE_APP_CLIENT_ID,
      },
      renderParams: {
        width: 250,
        height: 50,
        longtitle: true,
      },
      valid: true,
      loader: null,
      loading: false,
      rules: {
        emailRules: [
          (v) => !!v || "Email không được bỏ trống",
          (v) =>
            /^([_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*(\.[a-zA-Z]{1,6}))?$/.test(
              v
            ) || "Email không hợp lệ",
        ],
        pwdRules: [
          (v) => !!v || "Mật khẩu không được bỏ trống",
          (v) => v.length >= 8 || "Mật khẩu phải có độ dài lớn hơn 8 ký tự",
          (v) => v.length <= 25 || "Mật khẩu dài quá 25 ký tự",
          (v) =>
            /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$)/.test(
              v
            ) || "Mật khẩu phải chứa ký tự số, ký tự đặc biệt ký tự in hoa",
        ],
      },
      isShowPwd: false,
      validate: {
        email: {
          invalid: false,
          msg: "",
        },
        pwd: {
          invalid: false,
          msg: "",
        },
      },
    };
  },
  computed: {
    email: {
      get() {
        return this.$store.getters.email;
      },
      set(val) {
        this.setEmail(val);
      },
    },
    password: {
      get() {
        return this.$store.getters.password;
      },
      set(val) {
        this.setPassword(val);
      },
    },
  },
  methods: {
    ...mapActions(["setEmail", "setPassword", "login", "setAccessToken"]),
    onLogin: async function () {
      this.$refs.form.validate();
      if (!this.valid) {
        for (let ref in this.$refs) {
          if (this.$refs[ref].hasError) {
            this.$refs[ref].focus();
            break;
          }
        }
      } else {
        this.loader = "loading";
        this.loading = true;
        let result = await this.login();
        if (result && result.accessToken) {
          this.setAccessToken(result.accessToken);
          localStorage.setItem("accessToken", result.accessToken);
          this.$router.push({ name: "home" });
        } else {
          switch (result.code) {
            case "107":
              this.validate.pwd.invalid = true;
              this.validate.pwd.msg = result.message;
              this.$refs["pwd"].focus();
              break;
            case "108":
              this.validate.email.invalid = true;
              this.validate.email.msg = result.message;
              this.$refs["email"].focus();
              break;
          }
        }
        this.loader = null;
        this.loading = false;
      }
    },
    clearError(refName) {
      this.validate[refName].invalid = false;
      this.validate[refName].msg = "";
    },
    async onSuccess(googleUser) {
      try {
        const actor = googleUser.getBasicProfile();
        let user = {
          username: actor.MT,
          firstName: actor.rT,
          lastName: actor.wV,
          email: actor.ou,
          avatarUrl: actor.uK,
        };
        let { data, status } = await request("/api/auth/external", {
          method: "POST",
          data: user,
        });
        if (status == 200 && data) {
          this.setAccessToken(data.accessToken);
          localStorage.setItem("accessToken", data.accessToken);
          this.$router.push({ name: "home" });
        }
      } catch (error) {
        console.log(error);
      }
    },
    onFailure() {},
    onLoginFacebook() {
      this.$refs["facebookButton"].buttonClicked();
    },
    async getUserData(fbAuth) {
      try {
        if (
          fbAuth &&
          fbAuth.response.authResponse.userID &&
          fbAuth.response.authResponse.accessToken
        ) {
          let res = await axios.get(
            `${process.env.VUE_APP_FACEBOOK_URL}${fbAuth.response.authResponse.userID}?fields=${process.env.VUE_APP_FILEDS_FACEBOOK}&access_token=${fbAuth.response.authResponse.accessToken}`
          );
          if (res.data && res.status == 200) {
            let { data } = res;
            let user = {
              username: data.id,
              firstName: data.first_name,
              lastName: data.last_name,
              email: data.email,
            };
            if (data.picture && data.picture.data) {
              user.avatarUrl = data.picture.data.url;
            }
            let response = await request("/api/auth/external", {
              method: "POST",
              data: user,
            });
            if (response.status == 200 && response.data) {
              this.setAccessToken(response.data.accessToken);
              localStorage.setItem("accessToken", response.data.accessToken);
              this.$router.push({ name: "home" });
            }
          }
        }
      } catch (error) {
        console.log(error);
      }
    },
    onLogout() {},
  },
};
</script>

<style scoped>
.h-11 {
  height: 2.75rem;
}
.br-global {
  border-radius: 0.375rem;
}
.outline-none {
  outline: none;
}
.color-white {
  color: white !important;
}
.v-input__slot {
  border-color: #e2e8f0 !important;
}
</style>