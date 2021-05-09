package com.youngtify.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
    private String id;
    private String fullname;
    private String avatarUrl;
}
