<template>
  <div class="intro-y box px-5 py-8 mt-4">
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
        ></v-text-field>
      </v-form>
    </div>
    <div class="intro-y auth__info flex text-xs sm:text-sm mt-4">
      <div class="flex items-center mr-auto">
        <input type="checkbox" class="input border mr-2" id="remember-me" />
        <label class="cursor-pointer select-none" for="remember-me"
          >Remember me</label
        >
      </div>
      <a href="">Forgot Password?</a>
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
      <button
        class="intro-y button button--lg button--secondary w-full border mt-3"
      >
        Sign up
      </button>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "Login",
  data: () => {
    return {
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
  },
};
</script>

<style>
.outline-none {
  outline: none !important;
}
.color-white {
  color: white !important;
}
.v-input__slot{
  border-color: #e2e8f0 !important;
}
</style>