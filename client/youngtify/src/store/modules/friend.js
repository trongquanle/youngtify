import { request } from '@/api';

const state = {
    friendProfile: {
        firstName: '',
        lastName: '',
        gender: 0,
        dateOfBirth: undefined,
        phoneNumber: '',
        avatarUrl: ''
    }
}

const mutations = {
    'SET_FRIEND_PROFILE': (state, payload) => {
        state.friendProfile = payload;
    }
}

const actions = {
    setFriendProfile: async ({ commit }, payload) => {
        try {
            let { data } = await request(`/api/users/${payload.userId}`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${payload.accessToken}`
                }
            });
            if (data)
                commit("SET_FRIEND_PROFILE", data);
        } catch (error) {
            console.log(error.response);
        }
    }
}

export default {
    namespaned: true,
    state,
    mutations,
    actions
}