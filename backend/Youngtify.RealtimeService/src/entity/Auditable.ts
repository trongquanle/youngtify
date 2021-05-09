import { PrimaryGeneratedColumn, Column } from "typeorm";

export abstract class Auditable {
    @PrimaryGeneratedColumn("uuid", { name: 'Id' })
    id: string;
    @Column({ name: 'CreatedDate', type: 'timestamp' })
    createdDate: Date;
    @Column({ name: 'ModifiedDate', type: 'timestamp' })
    modifiedDate: Date;
}