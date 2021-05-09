import { request } from '@/api';
import moment from 'moment';

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
    loadding: true
}

const mutations = {
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
    },
    DELETE_ROOM: (state, payload) => {
        state.currentRoom = null;
        state.rooms = state.rooms.filter(room => room._id !== payload._id);
    },
    ASSIGN_SOCKET: (state, payload) => {
        state.sk = payload;
    },
    INIT_SOCKET_EVENTS: (state, payload) => {
        state.sk.emit("signCode", payload);
        state.sk.on("receivedCode", (code) => {
            // console.log(code);
            state.code = code;
        });
        state.sk.on("receivedMsg", (payload) => {
            let { data } = JSON.parse(payload);
            if (data.roomId === state.currentRoom) {
                if (state.messages.length > 0) {
                    let length = state.messages.length;
                    if(state.messages[length-1][0].senderId == data.senderId){
                        state.messages[length-1].push({
                            id: data.id,
                            senderId: data.senderId,
                            message: data.message,
                            messageType: data.messageType,
                            avatarUrl: "/images/profile-1.jpg",
                            date: moment("2021-02-21T01:00:00", "YYYY-MM-DD HH:mm:ss"),
                        });
                    }else{
                        let actor = [{
                            id: data.id,
                            senderId: data.senderId,
                            message: data.message,
                            messageType: data.messageType,
                            avatarUrl: "/images/profile-1.jpg",
                            date: moment("2021-02-21T01:00:00", "YYYY-MM-DD HH:mm:ss"),
                        }];
                        state.messages.push(actor);
                    }
                }
                state.rooms.map(room => {
                    if (room.conversationId == data.roomId)
                        room.messageTitle = data.messageTitle;
                })
            } else {
                // TODO: Thêm vào title ở room, set lại vị trí của list room
            }
        });
        state.sk.on("user_disconnect", (data) => {
            console.log(JSON.parse(data));
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
        }, 1000);
    },
    SET_MESSAGES: (state, payload) => {
        state.messages = [];
        let msgts = [];
        let actor = [];
        let id;
        payload.forEach((msg, index) => {
            if (!id) {
                id = msg.senderId;
            }
            if (id == msg.senderId) {
                actor.push(msg);
            } else {
                msgts.push(actor);
                actor = [msg];
                id = msg.senderId;
            }
            if (index == payload.length - 1) {
                msgts.push(actor);
            }
        });
        state.messages = msgts;
        state.scrollDown = true;
    },
    SET_MESSAGES_MORE: (state, payload) => {
        let msgts = [];
        let actor = [];
        let id;
        payload.forEach((msg, index) => {
            if (!id) {
                id = msg.senderId;
            }
            if (id == msg.senderId) {
                actor.push(msg);
            } else {
                msgts.push(actor);
                actor = [msg];
                id = msg.senderId;
            }
            if (index == payload.length - 1) {
                msgts.push(actor);
            }
        });
        state.messages = [...msgts, ...state.messages];
        state.scrollDown = false;
    },
    SET_KEY: (state, payload) => {
        state.key = payload;
    },
    ADD_MESSAGE: (state, payload) => {
        // if(state.messages && state.messages.length > 0){
        //     state.messages[state.messages.length-1][0].senderId
        // }
        state.messages.push(payload);
        state.scrollDown = true;
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
    }
}

const actions = {
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
    loadRoom: async ({ commit, state }) => {
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
                commit('SAVE_CURRENT_ROOM', res.data[0]['conversationId']);
                res = await request(`/api/messages/${state.currentRoom}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                    },
                });
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
    }
}

export default {
    namespaned: true,
    state,
    mutations,
    actions
}