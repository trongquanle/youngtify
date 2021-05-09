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
public class Message {
    private UUID id;
    private String message;
    private Integer messageType;
    private UUID senderId;
    private String avatarUrl;
    private Date modifiedDate;
}
