import {Entity, Column, PrimaryGeneratedColumn, Generated, CreateDateColumn, UpdateDateColumn} from "typeorm";
import { Auditable } from "./Auditable";

@Entity("messages")
export class Message extends Auditable{
    @Column({name: 'ConversationId'})
    conversationId: string;

    @Column({name: 'SenderId'})
    senderId: string;

    @Column({name: 'MessageType'})
    messageType: number;

    @Column({name: 'Message'})
    message: string;
}
