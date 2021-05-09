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
@Table(name = "conversations")
public class ConversationEntity extends Auditable {
    @Column(name = "CreatorId")
    @Type(type="uuid-char")
    private UUID creatorId;
    @Column(name = "ConversationType")
    private int conversationType;
    @Column(name = "Title")
    private String title;
}
