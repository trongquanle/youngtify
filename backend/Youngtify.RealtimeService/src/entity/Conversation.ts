import { Column, Entity } from "typeorm";
import { Auditable } from "./Auditable";

@Entity("conversations")
export class Conversation extends Auditable {
    @Column({name: 'Title'})
    title: string;
    @Column({name: 'CreatorId'})
    creatorId: string;
    @Column({name: 'ConversationType'})
    conversationType: number;
}