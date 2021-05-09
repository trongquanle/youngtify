import {Entity, Column} from "typeorm";
import { Auditable } from "./Auditable";

@Entity("activities")
export class Activity extends Auditable{

    @Column({name: 'Status'})
    status: number;

}
