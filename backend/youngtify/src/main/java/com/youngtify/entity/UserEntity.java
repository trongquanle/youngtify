package com.youngtify.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends Auditable {
    @Column(name = "Username", columnDefinition = "varchar(100)")
    private String username;
    @Column(name = "Email", columnDefinition = "varchar(100)")
    private String email;
    @Column(name = "Password")
    private String password;
    @Column(name = "IsActive")
    private boolean isActive;
}
