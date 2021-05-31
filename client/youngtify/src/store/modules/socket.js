import { request } from '@/api';
import moment from 'moment';
import io from 'socket.io-client';
import { v4 } from 'uuid';
import {getKey} from '@/util/cryptoMessage';

const state = {
    currentRoom: null,
    rooms: [],
    sk: null,
    code: null,
    messages: [],
    msg: null,
    key: null,
    scrollDown: null,
    loadMore: false,
    loadding: true,
    friendProfile: {
        firstName: '',
        lastName: '',
        gender: 0,
        dateOfBirth: undefined,
        phoneNumber: '',
        avatarUrl: ''
    },
    friendAtvs: [],
    currentTime: undefined,
    notification: {
        count: 0,
        data: []
    },
    keyRoom: undefined
}

const mutations = {
    INIT_CURRENT_TIME: (state) => {
        state.currentTime = moment();
        setInterval(() => {
            state.currentTime = moment();
            // console.log(state.currentTime.format("yyyy-MM-DD hh-mm-ss"));
        }, 1000);
    },
    SET_FRIEND_PROFILE: (state, payload) => {
        state.friendProfile = payload;
    },
    SET_MSG: (state, payload) => {
        state.msg = payload;
    },
    SET_CODE: (state, payload) => {
        state.code = payload
    },
    ASSIGN_ROOM_DATA: (state, payload) => {
        state.rooms = payload;
    },
    ADD_ROOM: (state, payload) => {
        state.rooms = [...state.rooms, payload];
    },
    SAVE_CURRENT_ROOM: (state, payload) => {
        state.currentRoom = payload;
        state.keyRoom = getKey(payload);
    },
    DELETE_ROOM: (state, payload) => {
        state.currentRoom = null;
        state.rooms = state.rooms.filter(room => room._id !== payload._id);
    },
    ASSIGN_SOCKET: (state, payload) => {
        state.sk = payload;
    },
    INIT_SOCKET_EVENTS: (state, payload) => {
        state.code = payload;
        state.sk = io(process.env.VUE_APP_REALTIME_URL);
        state.sk.on("signCodeClient", () => {
            state.sk.emit("signCode", state.code);
            state.sk.emit("getNotification");
        });
        state.sk.on("receivedCode", (code) => {
            state.code = code;
        });
        state.sk.on("receivedMsg", (payload) => {
            state.scrollDown = true;
            let { data } = JSON.parse(payload);
            if (data.roomId === state.currentRoom) {
                if (state.messages.length > 0) {
                    if (state.messages[0].data[0].senderId == data.senderId) {
                        state.messages[0].data.push({
                            id: data.id,
                            senderId: data.senderId,
                            message: data.message,
                            messageType: data.messageType,
                            avatarUrl: data.avatarUrl,
                            modifiedDate: data.modifiedDate,
                        });
                    } else {
                        let actor = {
                            id: v4(),
                            data: [{
                                id: data.id,
                                senderId: data.senderId,
                                message: data.message,
                                messageType: data.messageType,
                                avatarUrl: data.avatarUrl,
                                modifiedDate: data.modifiedDate,
                            }]
                        };
                        state.messages.unshift(actor)
                    }
                } else{
                    let actor = {
                        id: v4(),
                        data: [{
                            id: data.id,
                            senderId: data.senderId,
                            message: data.message,
                            messageType: data.messageType,
                            avatarUrl: data.avatarUrl,
                            modifiedDate: data.modifiedDate,
                        }]
                    };
                    state.messages.push(actor);
                }
                state.rooms.map(room => {
                    if (room.conversationId == data.roomId) {
                        room.messageTitle = data.messageTitle;
                        room.modifiedDate = data.modifiedDate;
                        room.senderId = data.senderId;
                        room.messageType = data.messageType;
                    }
                });
            } else {
                state.rooms.map(room => {
                    if (room.conversationId == data.roomId) {
                        room.messageTitle = data.messageTitle;
                        room.modifiedDate = data.modifiedDate;
                        room.senderId = data.senderId;
                        room.messageType = data.messageType;
                    }
                });
            }
        });
        state.sk.on("receivedFriendAtvs", (payload) => {
            if (payload && payload.length > 0) {
                state.friendAtvs = payload.map(item => {
                    // let atvText = 'Đang hoạt động';
                    // if (!item.status) {
                    //     const duration = moment.duration(moment().diff(moment(item["time"])));
                    //     if (duration.asMinutes() < 60) {
                    //         let minute = parseInt(duration.asMinutes());
                    //         atvText = `Hoạt động ${minute == 0 ? 1 : minute} phút trước`;
                    //     } else if (duration.asHours() < 24) {
                    //         atvText = `Hoạt động ${parseInt(duration.asHours())} giờ trước`;
                    //     } else if (duration.asDays() < 7) {
                    //         atvText = `Hoạt động ${parseInt(duration.asDays())} ngày trước`;
                    //     } else if (duration.asWeeks() < 4) {
                    //         atvText = `Hoạt động ${parseInt(duration.asWeeks())} tuần trước`;
                    //     } else if (duration.asMonths() < 12) {
                    //         atvText = `Hoạt động ${parseInt(duration.asMonths())} tháng trước`;
                    //     } else {
                    //         atvText = `Hoạt động ${parseInt(duration.asYears())} năm trước`;
                    //     }
                    // }
                    return {
                        id: item["id"],
                        status: item["status"],
                        time: item["time"]
                    };
                });
            }
        });
        state.sk.on("updateFriendAtvs", payload => {
            if (state.friendAtvs && state.friendAtvs.length > 0) {
                for (let i = 0; i < state.friendAtvs.length; i++) {
                    if (state.friendAtvs[i].id == payload.id) {
                        state.friendAtvs[i].status = payload.status;
                        state.friendAtvs[i].time = payload.time;
                        break;
                    }
                }
            }
        });
        state.sk.on("user_disconnect", (data) => {
            console.log(JSON.parse(data));
        });
        state.sk.on("getNotification", (payload) => {
            try {
                let { count, data } = JSON.parse(payload);
                state.notification.count = count;
                state.notification.data = data;
            } catch (error) {
                console.log(error);
            }
        });
        state.sk.on("receiverNotification", payload => {
            console.log(JSON.parse(payload));
            if(state.notification.data.length == 0){
                state.notification.data.push(JSON.parse(payload));
            }else{
                state.notification.data.unshift(JSON.parse(payload));
            }
            state.notification.count++;
        });
        state.sk.on("acceptVideoCall", (payload) => {

        });
        state.sk.on("rejectVideoCall", (payload) => {
            console.log(payload);
        });
    },
    LEAVE_ROOM: (state, payload) => {
        state.currentRoom.users = payload;
    },
    RESET_STATE: state => {
        state.currentRoom = null;
        state.rooms = [];
    },
    SET_ROOM: (state, payload) => {
        setTimeout(() => {
            state.rooms = payload;
        }, 0);
        // setInterval(() => {
        //     // [].reverse
        //     state.rooms = state.rooms.reverse();
        // }, 1000);
    },
    SET_MESSAGES: (state, payload) => {
        state.messages = [];
        let msgts = [];
        let actor = { id: v4(), data: [] };
        let id;
        payload.forEach((msg, index) => {
            if (!id) {
                id = msg.senderId;
            }
            if (id == msg.senderId) {
                actor.data.push(msg);
            } else {
                msgts.unshift(actor);
                actor = { id: v4(), data: [msg] };
                id = msg.senderId;
            }
            if (index == payload.length - 1) {
                msgts.unshift(actor);
            }
        });
        state.messages = msgts;
        state.scrollDown = true;
    },
    SET_MESSAGES_MORE: (state, payload) => {
        let msgts = [];
        let actor = { id: v4(), data: [] };
        let id;
        payload.forEach((msg, index) => {
            if (!id) {
                id = msg.senderId;
            }
            if (id == msg.senderId) {
                actor.data.push(msg);
            } else {
                msgts.unshift(actor);
                actor = { id: v4(), data: [msg] };
                id = msg.senderId;
            }
            if (index == payload.length - 1) {
                msgts.unshift(actor);
            }
        });
        state.messages = [...state.messages, ...msgts];
        state.scrollDown = false;
    },
    SET_KEY: (state, payload) => {
        state.key = payload;
    },
    ADD_MESSAGE: (state, payload) => {
        // if(state.messages && state.messages.length > 0){
        //     state.messages[state.messages.length-1][0].senderId
        // }
        state.scrollDown = true;
        state.messages.push(payload);
    },
    CHANGE_MSG_TITLE: (state, payload) => {
        state.rooms.map(room => {
            if (room.conversationId == payload.roomId)
                room.messageTitle = payload;
        })
    },
    SET_LOAD_MORE: (state, payload) => {
        state.loadMore = payload;
    },
    SET_LOADDING: (state, payload) => {
        state.loadding = payload;
    },
    SET_NOTIFICATION: (state) => {
        state.notification.count = 0;
        state.notification.data.map(notify => {
            notify.isSeen = 1;
        });
    },
    RESET_SOCKET: (state) => {
        state.currentRoom = null;
        state.rooms = [];
        state.sk = null;
        state.code = null;
        state.messages = [];
        state.msg = null;
        state.key = null;
        state.scrollDown = null;
        state.loadMore = false;
        state.loadding = true;
        state.friendProfile = {
            firstName: '',
            lastName: '',
            gender: 0,
            dateOfBirth: undefined,
            phoneNumber: '',
            avatarUrl: ''
        }
        state.friendAtvs = [];
        state.currentTime = undefined;
    }
}

const actions = {
    initCurrentTime: ({ commit }) => {
        commit("INIT_CURRENT_TIME");
    },
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
    setMsg: ({ commit }, payload) => {
        commit('SET_MSG', payload);
    },
    setCode: ({ commit }, payload) => {
        commit('SET_CODE', payload);
    },
    updateRoomData: ({ commit }, payload) => {
        commit('ASSIGN_ROOM_DATA', payload);
    },
    addRoom: ({ commit }, payload) => {
        commit('ADD_ROOM', payload);
    },
    deleteRoom: ({ commit }, payload) => {
        commit('DELETE_ROOM', payload);
    },
    assignSocket: ({ commit }, payload) => {
        commit('ASSIGN_SOCKET', payload);
    },
    initSocketEvents: ({ commit }, payload) => {
        commit("INIT_SOCKET_EVENTS", payload);
    },
    saveCurrentRoom: ({ commit }, payload) => {
        commit('SAVE_CURRENT_ROOM', payload);
    },
    leaveRoom: ({ commit }, payload) => {
        commit('REMOVE_USER_ID', payload);
    },
    connect: ({ commit }, payload) => {
        payload.on("connect", (data) => {
            payload.emit("join", "Hello server from client!");
        });
    },
    loadRoom: async ({ commit, dispatch, state }) => {
        try {
            commit('SET_LOADDING', true);
            let res = await request("/api/conversations", {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                },
            });
            commit('SET_ROOM', res.data)
            if (res.data.length > 0) {
                let id = res.data[0]['id'];
                commit('SAVE_CURRENT_ROOM', res.data[0]['conversationId']);
                res = await request(`/api/messages/${state.currentRoom}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                    },
                });
                dispatch('setFriendProfile', { accessToken: localStorage.getItem("accessToken"), userId: id });
                commit('SET_MESSAGES', res.data);
                if (res.data.length >= 10) {
                    let d = moment(res.data[0]['modifiedDate']).format('YYYY-MM-DD HH:mm:ss');
                    commit('SET_KEY', d);
                    commit('SET_LOAD_MORE', true);
                } else {
                    commit('SET_KEY', null);
                    commit('SET_LOAD_MORE', false);
                }
            }
        } catch (error) {
            console.log(error);
        } finally {
            setTimeout(() => {
                commit('SET_LOADDING', false);
            }, 1000);
        }
    },
    loadMessages: async ({ commit, state }) => {
        try {
            commit('SET_MESSAGES', []);
            commit('SET_KEY', null);
            commit('SET_LOAD_MORE', false);
            commit('SET_LOADDING', true);
            let { data } = await request(`/api/messages/${state.currentRoom}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                },
            });
            commit('SET_MESSAGES', data);
            if (data.length >= 10) {
                let d = moment(data[0]['modifiedDate']).format('YYYY-MM-DD HH:mm:ss');
                commit('SET_KEY', d);
                commit('SET_LOAD_MORE', true);
            } else {
                commit('SET_KEY', null);
                commit('SET_LOAD_MORE', false);
            }
        } catch (error) {
            console.log(error.response);
        } finally {
            setTimeout(() => {
                commit('SET_LOADDING', false);
            }, 1000);
        }
    },
    loadMoreMessages: async ({ commit, state }) => {
        try {
            let url = `/api/messages/${state.currentRoom}`;
            if (state.key) url = `${url}?key=${state.key}`;
            let { data } = await request(url, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                },
            });
            commit('SET_MESSAGES_MORE', data);
            if (data.length >= 10) {
                let d = moment(data[0]['modifiedDate']).format('YYYY-MM-DD HH:mm:ss')
                commit('SET_KEY', d);
                commit('SET_LOAD_MORE', true);
            } else {
                commit('SET_KEY', null);
                commit('SET_LOAD_MORE', false);
            }
        } catch (error) {
            console.log(error);
        }
    },
    addMessage: ({ commit }, payload) => {
        commit('ADD_MESSAGE', payload);
    },
    changeMsgTitle: ({ commit }, payload) => {
        commit('CHANGE_MSG_TITLE', payload);
    },
    setNotification: ({commit}) => {
        commit("SET_NOTIFICATION");
    },
    resetSocket: ({ commit }) => {
        commit("RESET_SOCKET");
    }
}

export default {
    namespaned: true,
    state,
    mutations,
    actions
}