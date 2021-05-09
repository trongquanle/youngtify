import { Column, Entity } from "typeorm";
import { Auditable } from "./Auditable";

@Entity("participants")
export class Participant extends Auditable {
    @Column({name: 'ConversationId'})
    conversationId: string;
    @Column({name: 'UserId'})
    userId: string;
}