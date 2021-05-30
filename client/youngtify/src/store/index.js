import Vue from 'vue';
import Vuex from 'vuex';
import auth from './modules/auth';
import socket from './modules/socket';
import image from './modules/image';
import getters from './getters';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        socket,
        auth,
        image
    },
    getters
});