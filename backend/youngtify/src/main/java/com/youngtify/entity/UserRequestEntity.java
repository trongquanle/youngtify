package com.youngtify.entity;

import com.youngtify.enumration.UserRequestStatus;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_requests")
public class UserRequestEntity extends Auditable{
    @Column(name = "SenderId", columnDefinition = "char(36)")
    @Type(type="uuid-char")
    private UUID senderId;
    @Column(name = "ReceiverId", columnDefinition = "char(36)")
    @Type(type="uuid-char")
    private UUID receiverId;
    @Column(name = "Status")
    private int status;
}
