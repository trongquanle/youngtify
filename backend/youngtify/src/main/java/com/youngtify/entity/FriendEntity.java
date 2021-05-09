package com.youngtify.entity;

import com.youngtify.enumration.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friends")
public class FriendEntity extends Auditable{
    @Column(name = "UserId", columnDefinition = "char(36)")
    @Type(type="uuid-char")
    private UUID userId;
    @Column(name = "FriendId", columnDefinition = "char(36)")
    @Type(type="uuid-char")
    private UUID friendId;
    @Column(name = "Status")
    private FriendStatus status;
    @Column(name = "UserActionId", columnDefinition = "char(36)")
    @Type(type="uuid-char")
    private UUID userActionId;
}
