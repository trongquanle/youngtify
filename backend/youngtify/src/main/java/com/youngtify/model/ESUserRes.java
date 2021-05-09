package com.youngtify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ESUserRes {
    private String id;
    private String fullname;
    private String phonenumber;
    private String email;
    private String avatar;
    private int status;
}
