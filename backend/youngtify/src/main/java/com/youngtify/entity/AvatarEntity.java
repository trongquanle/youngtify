package com.youngtify.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avatars")
public class AvatarEntity extends Auditable {
    @Column(name = "UserId", columnDefinition = "CHAR(36)")
    private UUID userId;
    @Column(name = "AvatarUrl", columnDefinition = "VARCHAR(255)")
    private String avatarUrl;
    @Column(name = "IsUsing", columnDefinition = "INT(2)")
    private int isUsing;
}
