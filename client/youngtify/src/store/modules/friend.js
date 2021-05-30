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
    SET_FRIEND_PROFILE: (state, payload) => {
        state.friendProfile = payload;
    },
    RESET_FRIEND: (state) => {
        state.friendProfile = {
            firstName: '',
            lastName: '',
            gender: 0,
            dateOfBirth: undefined,
            phoneNumber: '',
            avatarUrl: ''
        }
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
    },
    resetFriend: ({commit}) => {
        console.log(1);
        commit("RESET_FRIEND");
    }
}

export default {
    namespaned: true,
    state,
    mutations,
    actions
}