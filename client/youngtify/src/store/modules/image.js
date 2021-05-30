
const state = {
    imgs: [],
    index: 0,
    visible: false
}

const mutations = {
    SET_IMAGES: (state, payload) => {
        if (payload.imgs && payload.imgs.length > 0) {
            state.imgs = payload.imgs.map((img) => `${process.env.VUE_APP_STORAGE_URL}${img}`);
        } else state.imgs = [];
        state.index = payload?.index || 0;
    },
    SET_VISBLE: (state, payload) => {
        state.visible = payload;
    }
}

const actions = {
    setImages: ({ commit }, payload) => {
        commit("SET_IMAGES", payload);
    },
    setVisible: ({ commit }, payload) => {
        commit("SET_VISBLE", payload);
    }
}

export default {
    namespaned: true,
    state,
    mutations,
    actions
}