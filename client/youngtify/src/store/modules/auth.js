import { request } from '@/api';

const state = {
    email: '',
    password: '',
    profile: JSON.parse(localStorage.getItem('profile')) || {
        avatarUrl: '',
        dateOfBirth: '',
        email: '',
        firstName: '',
        gender: 0,
        id: '',
        lastName: '',
        phoneNumber: '',
        username: '',
    },
    accessToken: localStorage.getItem("accessToken")
}

const mutations = {
    'SET_EMAIL': (state, payload) => {
        state.email = payload;
    },
    'SET_PASSWORD': (state, payload) => {
        state.password = payload;
    },
    'SET_PROFILE': (state, payload) => {
        localStorage.setItem('profile', JSON.stringify(payload));
        state.profile = payload;
    },
    'SET_ACCESS_TOKEN': (state, payload) => {
        state.accessToken = payload;
    },
    RESET_AUTH: (state) => {
        state.email = '';
        state.password = '';
        state.profile = {
            avatarUrl: '',
            dateOfBirth: '',
            email: '',
            firstName: '',
            gender: 0,
            id: '',
            lastName: '',
            phoneNumber: '',
            username: '',
        }
        state.accessToken = undefined;
    }
}

const actions = {
    setEmail: ({ commit }, payload) => {
        commit('SET_EMAIL', payload);
    },
    setPassword: ({ commit }, payload) => {
        commit('SET_PASSWORD', payload);
    },
    login: ({ state }) => {
        return new Promise(async (reslove, reject) => {
            try {
                const { data } = await request('/api/auth/login', { method: 'POST', data: { username: state.email, password: state.password } });
                reslove(data);
            } catch (error) {
                reject(error.response);
            }
        })
    },
    setAccessToken: ({ commit }, payload) => {
        commit('SET_ACCESS_TOKEN', payload);
    },
    setProfile: async ({ commit, dispatch, state }) => {
        try {
            let { data } = await request("/api/users", {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${state.accessToken}`
                }
            });
            commit('SET_PROFILE', data);
            // commit("socket/INIT_SOCKET_EVENTS", state.profile.id);
            dispatch('initSocketEvents', state.profile.id, { root: true });
        } catch (error) {
            console.log(error.response);
        }
    },
    resetAuth: ({commit}) => {
        commit("RESET_AUTH");
    }
}

export default {
    namespaned: true,
    state,
    mutations,
    actions
}