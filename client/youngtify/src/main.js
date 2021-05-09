import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';
import io from 'socket.io-client';

import '@/assets/css/common/index.min.css';

const socket = io('http://localhost:3000');

store.dispatch('assignSocket', socket);

// window.onunload = () => {
//   localStorage.clear();
// };


Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
