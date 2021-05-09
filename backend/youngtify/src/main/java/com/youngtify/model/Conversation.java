package com.youngtify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    private UUID id;
    private UUID conversationId;
    private String title;
    private String messageTitle;
    private String avatarUrl;
    private Date modifiedDate;
}
