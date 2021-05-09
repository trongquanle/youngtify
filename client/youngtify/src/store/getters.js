const getters = {
    email: (state) => state.auth.email,
    password: (state) => state.auth.password,
    profile: (state) => state.auth.profile,
    avatar: (state) => {
        if (state.auth.profile && state.auth.profile.avatarUrl) {
            return `http://localhost:3000${state.auth.profile.avatarUrl}`;
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
    friendProfile: (state) => state.friend.friendProfile
}

export default getters;