import { Entity, Column, PrimaryColumn } from "typeorm";

@Entity("user_socket")
export class UserSocket {

    constructor(socketId: string, userId: string){
        this.socketId = socketId;
        this.userId = userId;
    }

    @Column({ name: 'SocketId' })
    @PrimaryColumn()
    socketId: string;

    @Column({ name: 'UserId' })
    userId: string;

}
