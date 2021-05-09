package com.youngtify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfile {
    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
//    private boolean isActive;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private Integer gender;
}