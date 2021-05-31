<template>
  <div class="notification-dropdown dropdown relative">
    <a
      class="notification-dropdown__toggler dropdown-toggle h-full flex items-center justify-center w-16 relative -mr-3 md:mr-0 md:border-l md:border-r"
      @click="onShowNotification"
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
      >
        <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
        <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
      </svg>
      <div
        v-show="notification.count"
        class="notification-dropdown__toggler__count flex items-center justify-center absolute top-0 right-0 rounded-full"
      >
        {{ notification.count }}
      </div>
    </a>
    <div
      class="-intro-y dropdown-menu show border-gray-400 mt-1"
      style="
        width: 350px;
        position: absolute;
        left: -144px;
        border: 1px solid #484d5a;
      "
      v-show="showNotification"
      v-click-outside="onHideNotification"
    >
      <div class="dropdown-menu__content box dark:bg-dark-2 px-2 pt-4 pb-5">
        <div class="text-base font-medium leading-tight px-2">Thông báo</div>
        <div v-if="notification.data.length == 0" class="w-full flex justify-center py-4">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            version="1.1"
            id="Layer_1"
            width="144"
            height="144"
            x="0px"
            y="0px"
            viewBox="0 0 512 512"
            style="enable-background: new 0 0 512 512"
            xml:space="preserve"
            fill="#e2e8f0"
          >
            <g>
              <g>
                <path
                  d="M448.866,376.488l-22.768-173.029c-9.245-70.282-60.803-127.396-129.248-144.087V40.851    C296.85,18.325,278.525,0,255.999,0s-40.851,18.325-40.851,40.851v18.521c-68.447,16.689-120.004,73.804-129.25,144.087    L63.13,376.488c-19.074,3.434-33.595,20.143-33.595,40.193v32.681c0,4.513,3.658,8.17,8.17,8.17h297.204    c-18.966,23.81-48.039,38.128-78.911,38.128c-22.006,0-42.911-6.965-60.454-20.14c-3.609-2.709-8.73-1.98-11.439,1.626    c-2.709,3.607-1.982,8.73,1.626,11.439C206.127,503.903,230.424,512,255.998,512c40.081,0,77.555-20.757,98.949-54.468h119.348    c4.513,0,8.17-3.657,8.17-8.17v-32.681C482.465,396.631,467.942,379.92,448.866,376.488z M466.125,441.191H45.875v-24.511    c0-13.515,10.994-24.511,24.506-24.511h273.19c4.513,0,8.17-3.657,8.17-8.17s-3.657-8.17-8.17-8.17H79.698l22.402-170.239    c8.628-65.588,57.995-118.501,122.844-131.667c3.808-0.772,6.545-4.12,6.545-8.007V40.851c0-13.515,10.995-24.511,24.511-24.511    c13.515,0,24.511,10.996,24.511,24.511v25.066c0,3.886,2.737,7.234,6.545,8.007C351.903,87.09,401.271,140.003,409.9,205.591    l22.399,170.239h-56.047c-4.513,0-8.17,3.657-8.17,8.17s3.657,8.17,8.17,8.17h65.328c13.551,0.002,24.544,10.997,24.544,24.511    V441.191z"
                />
              </g>
            </g>
            <g>
              <g>
                <path
                  d="M255.998,32.681c-4.512,0-8.17,3.657-8.17,8.17v21.787c0,4.513,3.658,8.17,8.17,8.17c4.513,0,8.17-3.657,8.17-8.17V40.851    C264.168,36.338,260.511,32.681,255.998,32.681z"
                />
              </g>
            </g>
            <g>
              <g>
                <circle cx="309.063" cy="182.74" r="8.17" />
              </g>
            </g>
            <g>
              <g>
                <circle cx="352.637" cy="204.528" r="8.17" />
              </g>
            </g>
            <g>
              <g>
                <circle cx="159.363" cy="313.464" r="8.17" />
              </g>
            </g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
            <g></g>
          </svg>
        </div>
        <div
          v-for="notify in notification.data"
          :key="notify.id"
        >
        <NotificationItem :notify="notify"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  name: "Notification",
  data() {
    return {
      showNotification: false,
      isClickOuside: false,
    };
  },
  components: {
    NotificationItem: () => import("./NotificationItem")
  },
  computed: { ...mapGetters(["notification", "socket"]) },
  created(){
    // console.log(this.notification);
  },
  methods: {
    ...mapActions(["setNotification"]),
    onShowNotification() {
      this.showNotification = !this.showNotification;
      if (this.showNotification) {
        setTimeout(() => {
          this.isClickOuside = true;
        }, 0);
      }
    },
    onHideNotification() {
      if (this.isClickOuside) {
        this.showNotification = false;
        this.isClickOuside = false;
        if(this.notification.count > 0){
          this.socket.emit("seenNotification");
          this.setNotification();
        }
      }
    },
  },
};
</script>

<style>
</style>