package com.youngtify.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participants")
public class ParticipantEntity extends Auditable{
    @Column(name = "ConversationId", columnDefinition = "char(36)")
    @Type(type="uuid-char")
    private UUID conversationId;
    @Column(name = "UserId", columnDefinition = "char(36)")
    @Type(type="uuid-char")
    private UUID userId;
}
