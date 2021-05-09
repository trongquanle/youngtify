package com.youngtify.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", columnDefinition = "char(36)", nullable = false)
    @Type(type="uuid-char")
    private UUID id;

    @Column(name = "CreatedDate", columnDefinition = "timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)")
    private Date createdDate;

    @Column(name = "ModifiedDate", columnDefinition = "timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)")
    private Date modifiedDate;
}
