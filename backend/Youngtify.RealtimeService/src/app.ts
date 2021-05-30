import { createServer, Server } from 'http';
import * as express from 'express';
import * as dotenv from 'dotenv';
import { Server as SocketServer } from 'socket.io';
import { getConnection, QueryRunner, getManager, EntityManager } from "typeorm";
import { Activity } from './entity/Activity';
import { SocketResult } from './model/SocketResult';
import * as sql from './constant/sql';
import { v4 as uuidv4 } from 'uuid';
import * as redis from 'redis';
import { UserSocket } from './entity/UserSocket';

export class App {
    public static readonly PORT: number = 3000;
    private app: express.Application;
    private server: Server;
    private io: SocketServer;
    private port: any;
    private queryRunner: QueryRunner;
    private entityManager: EntityManager;
    // private keys = [];
    private client: any;

    constructor() {
        this.startup();
        this.listen();
        this.initRepository();
        this.client = redis.createClient({
            host: process.env.REDIS_HOST,
            port: parseInt(process.env.REDIS_PORT)
        });
    }

    private async initRepository() {
        this.queryRunner = getConnection().createQueryRunner();
        await this.queryRunner.connect();
        this.entityManager = getManager();
        await this.entityManager.clear(UserSocket);
    }

    private startup(): void {
        dotenv.config({
            path: '.env'
        })
        this.app = express();
        this.server = createServer(this.app);
        this.port = process.env.PORT || App.PORT;
        this.io = new SocketServer(this.server, {
            cors: {
                origin: "*",
                credentials: true
            }
        });
    }

    private listen(): void {
        this.server.listen(this.port, () => {
            console.log(`Server is running on port ${this.port}`);
        });

        this.io.on('connect', async (socket: any) => {
            this.io.in(socket.id).emit('signCodeClient');
            // Lấy danh sách phòng sau đó join
            socket.on('signCode', async (code: string) => {
                socket.code = code;
                // this.keys.push(code);
                let date = new Date();
                this.changeStatusActivity(code, 1, date, socket.id);
                let roomIds = await this.getRoomId(socket.code);
                if (roomIds) {
                    roomIds.forEach(roomId => {
                        socket.join(roomId);
                    });
                }
                this.io.in(socket.id).emit('receivedCode', code);
                console.log(code);
                let friendIds = null;
                try {
                    const rawData = await this.entityManager
                        .query(sql.procGetListFriendIds, [code, 1]);
                    friendIds = rawData[0].map(raw => raw["Id"]);
                } catch (error) {
                    console.log(error);
                }
                if (friendIds && friendIds.length > 0) {
                    const rawAtvs = await this.entityManager
                        .query(sql.sqlGetFriendAvtivites, [friendIds]);
                    const friendAtvs = rawAtvs.map(raw => {
                        return {
                            id: raw["Id"],
                            status: raw["Status"],
                            time: raw["ModifiedDate"]
                        }
                    });
                    this.io.in(socket.id).emit('receivedFriendAtvs', friendAtvs);
                    const friendSockets = await this.entityManager.query(sql.sqlGetFriendSocketByUserIds, [friendIds]);
                    if (friendSockets && friendSockets.length > 0) {
                        friendSockets.forEach(item => {
                            this.io.in(item["SocketId"]).emit('updateFriendAtvs', {
                                id: code,
                                status: 1,
                                time: date
                            });
                        });
                    }
                }
            });
            socket.on('sendMsg', async (payload) => {
                let { msg, roomId, messageType, code } = JSON.parse(payload);
                if (!socket.code) socket.code = code;
                let date = new Date();
                this.queryRunner.startTransaction();
                try {
                    let msgId = uuidv4();
                    this.queryRunner.manager.query(sql.sqlInsertMsg, [msgId, roomId, msg, messageType, socket.code]);
                    let msgTitle = msg;
                    if (messageType == 1) {
                        let numberImg = msg.split(",").length;
                        msgTitle = `đã gửi ${numberImg} ảnh`;
                    }
                    this.queryRunner.manager.query(sql.sqlUpdateParticipants, [msgTitle, date, roomId]);
                    this.queryRunner.commitTransaction();
                    let response = new SocketResult(true, {
                        id: msgId,
                        roomId,
                        senderId: socket.code,
                        message: msg,
                        messageTitle: msgTitle,
                        modifiedDate: date,
                        messageType: messageType
                    })
                    this.io.to(roomId).emit('receivedMsg', JSON.stringify(response));
                } catch (error) {
                    // console.log(error);
                    this.io.to(roomId).emit('receivedMsg', JSON.stringify(new SocketResult(false, null)));
                    this.queryRunner.rollbackTransaction();
                }
            });

            socket.on('joinRoom', async (payload) => {
                console.log(payload);
                let { roomId } = JSON.parse(payload);
                // console.log(roomId);
                socket.join(roomId);
            });

            socket.on('disconnect', async () => {
                // this.keys = this.keys.filter(k => k != socket.code);
                let date = new Date();
                this.changeStatusActivity(socket.code, 0, date, socket.id);
                // console.log(`disconnect: ${socket.code}`);
                // this.io.emit('user_disconnect', JSON.stringify(this.keys));
                if (socket.code) {
                    this.client.del(socket.code);
                }
                let friendIds = null;
                try {
                    const rawData = await this.entityManager
                        .query(sql.procGetListFriendIds, [socket.code, 1]);
                    friendIds = rawData[0].map(raw => raw["Id"]);
                } catch (error) {
                    console.log(error);
                }
                if (friendIds && friendIds.length > 0) {
                    const friendSockets = await this.entityManager.query(sql.sqlGetFriendSocketByUserIds, [friendIds]);
                    if (friendSockets && friendSockets.length > 0) {
                        friendSockets.forEach(item => {
                            this.io.in(item["SocketId"]).emit('updateFriendAtvs', {
                                id: socket.code,
                                status: 0,
                                time: date
                            });
                        });
                    }
                }
            });
        });
    }

    public getApp(): express.Application {
        return this.app;
    }

    private async changeStatusActivity(code: string, status: number, date: Date, socketId: string): Promise<void> {
        let activity = new Activity();
        activity.id = code;
        activity.status = status;
        activity.modifiedDate = date;
        this.entityManager.save(Activity, activity);
        if (status == 1) {
            const userSocket = new UserSocket(socketId, code);
            await this.entityManager.save(UserSocket, userSocket);
        } else await this.entityManager.delete(UserSocket, socketId);
    }

    private async getRoomId(userId: string): Promise<Array<string>> {
        try {
            const rawData = await this.entityManager
                .query(sql.sqlGetRoomByUserId, [userId, 10]);
            return rawData.map(item => item['ConversationId']);
        } catch (error) {
            return null;
        }
    }

}