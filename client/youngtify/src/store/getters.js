const getters = {
    email: (state) => state.auth.email,
    password: (state) => state.auth.password,
    profile: (state) => state.auth.profile,
    avatar: (state) => {
        if (state.auth.profile && state.auth.profile.avatarUrl) {
            if (state.auth.profile.avatarUrl.search("https://") !== -1)
                return state.auth.profile.avatarUrl;
            return `${process.env.VUE_APP_STORAGE_URL}${state.auth.profile.avatarUrl}`;
        } else return undefined;
    },
    fullname: (state) => {
        return `${state.auth.profile.lastName} ${state.auth.profile.firstName}`;
    },
    name: (state) => {
        let temp = state.auth.profile.lastName[0] + state.auth.profile.firstName[0];
        return temp;
    },
    accessToken: (state) => state.auth.accessToken,
    socket: (state) => state.socket.sk,
    code: (state) => state.socket.code,
    msg: (state) => state.socket.msg,
    currentRoom: (state) => state.socket.currentRoom,
    rooms: (state) => state.socket.rooms,
    messages: (state) => state.socket.messages,
    key: (state) => state.socket.key,
    scrollDown: (state) => state.socket.scrollDown,
    loadMore: (state) => state.socket.loadMore,
    loadding: (state) => state.socket.loadding,
    friendProfile: (state) => state.socket.friendProfile,
    friendAtvs: (state) => state.socket.friendAtvs,
    currentTime: (state) => state.socket.currentTime,
    notification: (state) => state.socket.notification,
    keyRoom: (state) => state.socket.keyRoom,
    imgs: (state) => state.image.imgs,
    visible: (state) => state.image.visible,
    index: (state) => state.image.index
}

export default getters;