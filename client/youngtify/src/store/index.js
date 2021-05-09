import Vue from 'vue';
import Vuex from 'vuex';
import auth from './modules/auth';
import socket from './modules/socket';
import friend from './modules/friend';
import getters from './getters';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        auth,
        socket,
        friend
    },
    getters
});