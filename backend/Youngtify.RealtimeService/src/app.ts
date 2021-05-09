import { createServer, Server } from 'http';
import * as express from 'express';
import * as dotenv from 'dotenv';
import { Server as SocketServer } from 'socket.io';
import { getConnection, QueryRunner, getManager, EntityManager } from "typeorm";
import { Activity } from './entity/Activity';
import { SocketResult } from './model/SocketResult';
import * as sql from './constant/sql';
import {v4 as uuidv4} from 'uuid';

export class App {
    public static readonly PORT: number = 3000;
    private app: express.Application;
    private server: Server;
    private io: SocketServer;
    private port: any;
    private queryRunner: QueryRunner;
    private entityManager: EntityManager;
    private keys = []

    constructor() {
        this.startup();
        this.listen();
        this.initRepository();
    }

    private async initRepository(){
        this.queryRunner = getConnection().createQueryRunner();
        await this.queryRunner.connect();
        this.entityManager = getManager();
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
            // console.log(socket.id);
            // Lấy danh sách phòng sau đó join
            socket.on('signCode', async (code: string) => {
                socket.code = code;
                this.keys.push(code);
                this.changeStatusActivity(code, 1);
                let roomIds = await this.getRoomId(socket.code);
                roomIds.forEach(roomId => {
                    socket.join(roomId);
                });
                this.io.in(socket.id).emit('receivedCode', code);
            });
            socket.on('sendMsg', async (payload) => {
                let { msg, roomId, messageType } = JSON.parse(payload);
                let date = new Date();
                this.queryRunner.startTransaction();
                try {
                    let msgId = uuidv4();
                    this.queryRunner.manager.query(sql.sqlInsertMsg, [msgId, roomId, msg, messageType, socket.code]);
                    let msgTitle = msg;
                    if(messageType == 1){
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
                        date,
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
                console.log(roomId);
                socket.join(roomId);
            });

            socket.on('disconnect', async () => {
                this.keys = this.keys.filter(k => k != socket.code);
                this.changeStatusActivity(socket.code, 0);
                console.log(`disconnect: ${socket.code}`);
                this.io.emit('user_disconnect', JSON.stringify(this.keys));
            });
        });
    }

    public getApp(): express.Application {
        return this.app;
    }

    private changeStatusActivity(id: string, status: number): void {
        let activity = new Activity();
        activity.id = id;
        activity.status = status;
        if (id)
            this.entityManager.save(Activity, activity);
    }

    private async getRoomId(userId: string): Promise<Array<string>>{
        const rawData = await this.entityManager
                            .query(sql.sqlGetRoomByUserId, [userId, 10]);
        return rawData.map(item => item['ConversationId']);
    }

}