import {Entity, Column} from "typeorm";
import { Auditable } from "./Auditable";

@Entity("users")
export class User extends Auditable{

    @Column({name: 'Username'})
    username: string;

    @Column({name: 'Email'})
    email: string;

    @Column({name: 'Password'})
    password: string;

    @Column({name: 'IsActive'})
    isActive: boolean;
}
