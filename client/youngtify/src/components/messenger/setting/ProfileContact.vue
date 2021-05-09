<template>
  <div class="mt-6">
    <div class="w-full flex">
      <v-text-field
        label="Email*"
        disabled
        outlined
        required
        dense
        v-model="email"
      ></v-text-field>
    </div>
    <div class="w-full flex">
      <v-text-field
        label="Số điện thoại*"
        required
        dense
        outlined
        v-model="phoneNumber"
      ></v-text-field>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "ProfileContact",
  data: () => ({
    email: undefined,
    phoneNumber: undefined,
  }),
  computed: {
    ...mapGetters(["profile"]),
  },
  created() {
    this.email = this.profile.email;
    this.phoneNumber = this.profile.phoneNumber;
  },
  methods: {
    getVal: function () {
      let { phoneNumber } = this;
      return { phoneNumber };
    },
  },
};
</script>