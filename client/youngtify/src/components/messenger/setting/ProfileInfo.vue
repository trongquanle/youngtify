<template>
  <div>
    <div style="grid-gap: 16px" class="w-full flex mt-6">
      <div class="w-1/2">
        <v-text-field
          label="Họ và tên đệm*"
          required
          dense
          outlined
          v-model="lastName"
        ></v-text-field>
      </div>
      <div class="w-1/2">
        <v-text-field
          label="Tên*"
          persistent-hint
          required
          dense
          outlined
          v-model="firstName"
        ></v-text-field>
      </div>
    </div>
    <v-menu
      ref="menu"
      v-model="menu"
      :close-on-content-click="false"
      :return-value.sync="dateOfBirth"
      transition="scale-transition"
      offset-y
      min-width="auto"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-text-field
          v-model="dateOfBirth"
          label="Ngày sinh"
          readonly
          v-bind="attrs"
          v-on="on"
          dense
          outlined
        ></v-text-field>
      </template>
      <v-date-picker v-model="dateOfBirth" no-title scrollable>
        <v-spacer></v-spacer>
        <v-btn text color="primary" @click="menu = false"> Cancel </v-btn>
        <v-btn text color="primary" @click="$refs.menu.save(dateOfBirth)">
          OK
        </v-btn>
      </v-date-picker>
    </v-menu>
    <v-select
      item-text="val"
      item-value="id"
      :items="items"
      v-model="gender"
      label="Giới tính"
      outlined
      dense
    ></v-select>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "ProfileInfo",
  data: () => ({
    firstName: undefined,
    lastName: undefined,
    dateOfBirth: undefined,
    menu: false,
    items: [
      { id: 1, val: "Nam" },
      { id: 2, val: "Nữ" },
      { id: 0, val: "Khác" },
    ],
    gender: undefined,
  }),
  computed: {
    ...mapGetters(["profile"]),
  },
  created() {
    console.log("info");
    this.dateOfBirth = this.profile.dateOfBirth?.substr(0, 10);
    this.firstName = this.profile.firstName;
    this.lastName = this.profile.lastName;
    this.gender = this.profile.gender;
  },
  methods: {
    getVal: function () {
      let { firstName, lastName, dateOfBirth, gender } = this;
      return { firstName, lastName, dateOfBirth, gender };
    },
  },
};
</script>

<style>
</style>