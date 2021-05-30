const express = require('express');
require('dotenv').config();
const mariadb = require('mariadb');
const { v4: uuidv4 } = require('uuid');
const cors = require("cors");

const app = express();
const server = require('http').createServer(app);
const sql = require("./src/constant/sql");
const { uploadAvatar, uploadImage } = require("./src/helper/uploadImages");

app.use(express.static('public'));
app.use(cors());

app.post("/api/storage", uploadAvatar.single("file"), (req, res) => {
    try {
        let { file } = req;
        let filename;
        if (!file) filename = null;
        else filename = `/avatars/${file.filename}`;
        return res.status(200).json({ fileName: filename });
    } catch (error) {
        return res.status(200).json({ fileName: null });
    }
});

app.post("/api/storage/multiple", uploadImage.array("files", 12), (req, res) => {
    try {
        let { files } = req;
        let filenames = []
        if (files && files.length > 0) {
            filenames = files.map(file => `/images/${file.filename}`);
        }
        return res.status(200).json({ fileNames: filenames });
    } catch (error) {
        return res.status(200).json({ fileNames: null });
    }
})

const pool = mariadb.createPool({
    host: process.env.MARIADB_SERVER,
    user: process.env.MARIADB_LOGIN,
    password: process.env.MARIADB_PASSWORD,
    database: process.env.MARIADB_DATABASE,
    connectionLimit: 5
});


const io = require('socket.io')(server, {
    cors: {
        origin: "*",
        credentials: true
    }
});

const vcIo = io.of("/videocall");

pool.getConnection().then(conn => {
    io.on('connect', async (socket) => {
        io.in(socket.id).emit('signCodeClient');
        // Lấy danh sách phòng sau đó join
        socket.on('signCode', async (code) => {
            socket.code = code;
            // this.keys.push(code);
            let date = new Date();
            await changeStatusActivity(conn, code, 1, date, socket.id);
            let roomIds = await getRoomId(conn, socket.code);
            if (roomIds) {
                roomIds.forEach(roomId => {
                    socket.join(roomId);
                });
            }
            io.in(socket.id).emit('receivedCode', code);
            let friendIds = null;
            try {
                const rawData = await conn.query(sql.procGetListFriendIds, [code, 1]);
                friendIds = rawData[0].map(raw => raw["Id"]);
            } catch (error) {
                console.log(error);
            }
            if (friendIds && friendIds.length > 0) {
                const rawAtvs = await conn.query(sql.sqlGetFriendAvtivites, [friendIds]);
                const friendAtvs = rawAtvs.map(raw => {
                    return {
                        id: raw["Id"],
                        status: raw["Status"],
                        time: raw["ModifiedDate"]
                    }
                });
                io.in(socket.id).emit('receivedFriendAtvs', friendAtvs);
                const friendSockets = await conn.query(sql.sqlGetFriendSocketByUserIds, [friendIds]);
                if (friendSockets && friendSockets.length > 0) {
                    friendSockets.forEach(item => {
                        io.in(item["SocketId"]).emit('updateFriendAtvs', {
                            id: code,
                            status: 1,
                            time: date
                        });
                    });
                }
            }
        });
        socket.on('sendMsg', async (payload) => {
            let { msg, roomId, messageType, code, avatarUrl } = JSON.parse(payload);
            if (!socket.code) socket.code = code;
            let date = new Date();
            try {
                let msgId = uuidv4();
                await conn.query(sql.sqlInsertMsg, [msgId, roomId, msg, messageType, socket.code]);
                let msgTitle = msg;
                switch (messageType) {
                    case 1:
                        let numberImg = msg.split(",").length;
                        msgTitle = `đã gửi ${numberImg} ảnh`;
                        break;
                    case 2:
                        msgTitle = null;
                }
                await conn.query(sql.sqlUpdateParticipants, [msgTitle, date, messageType, socket.code, roomId]);
                let response = {
                    isSuccess: true,
                    data: {
                        id: msgId,
                        roomId,
                        senderId: socket.code,
                        message: msg,
                        messageTitle: msgTitle,
                        modifiedDate: date,
                        messageType: messageType,
                        avatarUrl: avatarUrl
                    }
                }
                io.to(roomId).emit('receivedMsg', JSON.stringify(response));
            } catch (error) {
                console.log(error);
                io.to(roomId).emit('receivedMsg', JSON.stringify({ isSuccess: false, data: null }));
            }
        });
        socket.on('sendNotification', async (payload) => {
            try {
                let { postId, userId, content } = JSON.parse(payload);
                const date = new Date();
                await conn.query(sql.sqlInsertNotification, [postId, userId, content, date]);
                const rows = await conn.query(sql.sqlGetSocketIdByUserId, [userId]);
                if (rows.length > 0) {
                    const socketId = rows[0]["SocketId"];
                    io.in(socketId).emit("receiverNotification", payload);
                }
            } catch (error) {
                console.log(error);
            }
        });
        socket.on('getNotification', async () => {
            const notification = {
                count: 0,
                data: []
            }
            try {
                const count = await conn.query(sql.sqlGetNotificationNotSeen, socket.code);
                if (count.length > 0) {
                    notification.count = count[0]["Count"];
                }
                const rows = await conn.query(sql.sqlGetNotifications, [socket.code]);
                if (rows.length > 0) {
                    const data = rows.map(row => {
                        return {
                            id: row["id"],
                            fullName: row["fullName"],
                            avatarUrl: row["avatarUrl"],
                            content: row["content"],
                            isSeen: row["isSeen"],
                            modifiedDate: row["modifiedDate"]
                        }
                    });
                    notification.data = data
                }
            } catch (error) {
                console.log(error);
            }
            io.in(socket.id).emit('getNotification', JSON.stringify(notification));
        });

        socket.on('videoCall', async (payload) => {
            const { userId } = payload;
            try {
                const rawData = await conn.query(sql.sqlGetSocketIdByUserId, [userId]);
                if (rawData.length > 0) {
                    const socketId = rawData[0]["SocketId"];
                    io.in(socketId).emit('receiverVideoCall', payload)
                }
            } catch (error) {
                console.log(error);
            }
        });

        socket.on('joinRoom', async (payload) => {
            console.log(payload);
            let { roomId } = JSON.parse(payload);
            // console.log(roomId);
            socket.join(roomId);
        });

        socket.on('disconnect', async () => {
            let date = new Date();
            changeStatusActivity(conn, socket.code, 0, date, socket.id);
            let friendIds = null;
            try {
                const rawData = await conn.query(sql.procGetListFriendIds, [socket.code, 1]);
                friendIds = rawData[0].map(raw => raw["Id"]);
            } catch (error) {
                console.log(error);
            }
            if (friendIds && friendIds.length > 0) {
                const friendSockets = await conn.query(sql.sqlGetFriendSocketByUserIds, [friendIds]);
                if (friendSockets && friendSockets.length > 0) {
                    friendSockets.forEach(item => {
                        io.in(item["SocketId"]).emit('updateFriendAtvs', {
                            id: socket.code,
                            status: 0,
                            time: date
                        });
                    });
                }
            }
        });
    });

    vcIo.on('connect', async (socket) => {
        socket.on('createRoomVideoCall', async (payload) => {
            try {
                await conn.query(sql.sqlInsertVideocall, [payload.senderId, payload.userId, socket.id, payload.roomId]);
            } catch (error) {
                console.log(error);
            }
        });
        socket.on('userConnectRoom', async (payload) => {
            const roomId = payload.roomId;
            try {
                await conn.query(sql.sqlUpdateVideocall, [socket.id, roomId]);
            } catch (error) {
                console.log(error);
            }
        });
        socket.on('finishVideoCall', async (payload) => {
            // Gửi về đầu bên kia sự kiện dừng video call
            // Xóa bản ghi bằng roomId
            const {roomId, userId} = payload
            try {
                const rowData = await conn.query(sql.sqlGetVideoCallByRoomId, [roomId]);
                if(rowData.length > 0){
                    const row = rowData[0];
                    const userVideoCall = {
                        id: row["Id"],
                        senderId: row["SenderId"],
                        userId: row["UserId"],
                        senderSocketId: row["SenderSocketId"],
                        userSocketId: row["UserSocketId"],
                        roomId: row["RoomId"]
                    };
                    const socketId = userVideoCall.senderId == userId ? userVideoCall.userSocketId : userVideoCall.senderSocketId;
                    vcIo.in(socketId).emit('onFinishVideoCall');
                    await conn.query(sql.sqlDeleteVideoCallById, [userVideoCall.id]);
                }
            } catch (error) {
                console.log(error);
            }
        });
        socket.on('rejectVideoCall', async (payload) => {
            const {roomId} = payload;
            try {
                const rowData = await conn.query(sql.sqlGetVideoCallByRoomId, [roomId]);
                if(rowData.length > 0){
                    const row = rowData[0];
                    const userVideoCall = {
                        id: row["Id"],
                        senderSocketId: row["SenderSocketId"]
                    };
                    vcIo.in(userVideoCall.senderSocketId).emit('rejectVideoCall');
                    await conn.query(sql.sqlDeleteVideoCallById, [userVideoCall.id]);
                }
            } catch (error) {
                console.log(error);
            }
        });
        socket.on('timeoutVideoCall', async (payload) => {
            const {roomId} = payload;
            try {
                const rowData = await conn.query(sql.sqlGetVideoCallByRoomId, [roomId]);
                if(rowData.length > 0){
                    const row = rowData[0];
                    const userVideoCall = {
                        id: row["Id"],
                        senderSocketId: row["SenderSocketId"]
                    };
                    vcIo.in(userVideoCall.senderSocketId).emit('timeoutVideoCall');
                    await conn.query(sql.sqlDeleteVideoCallById, [userVideoCall.id]);
                }
            } catch (error) {
                console.log(error);
            }
        });
        socket.on('disconnect', async () => {
            try {
                const rowData = await conn.query(sql.sqlGetVideoCallBySocketId, [socket.id, socket.id]);
                if(rowData.length > 0){
                    const row = rowData[0];
                    const userVideoCall = {
                        id: row["Id"],
                        senderId: row["SenderId"],
                        userId: row["UserId"],
                        senderSocketId: row["SenderSocketId"],
                        userSocketId: row["UserSocketId"],
                        roomId: row["RoomId"]
                    };
                    const socketId = userVideoCall.userSocketId == socket.id ? userVideoCall.senderSocketId : userVideoCall.userSocketId;
                    vcIo.in(socketId).emit('onFinishVideoCall');
                    await conn.query(sql.sqlDeleteVideoCallById, [userVideoCall.id]);
                }
            } catch (error) {
                console.log(error);
            }
        })
    })
}).catch(err => console.log(err))

const changeStatusActivity = async (conn, code, status, date, socketId) => {
    await conn.query(sql.sqlUpdateActivity, [status, date, code]);
    if (status == 1) {
        await conn.query(sql.sqlInsertUserSocket, [code, socketId]);
    } else await conn.query(sql.sqlDeleteUserSocket, [socketId]);
}
const getRoomId = async (conn, userId) => {
    try {
        const rawData = await conn
            .query(sql.sqlGetRoomByUserId, [userId, 10]);
        return rawData.map(item => item['ConversationId']);
    } catch (error) {
        return null;
    }
}

const sendNotification = async (conn, postId, userId, content, date) => {
    try {
        await conn.query(sql.sqlInsertNotification, [postId, userId, content, date]);
        return {
            postId,
            userId,
            content,
            date
        }
    } catch (error) {
        return null;
    }
}

server.listen(process.env.PORT || 3000, async () => {
    console.log('Server is running...');
    const conn = await pool.getConnection();
    await conn.query(sql.sqlTruncateUserSocket);
})