package com.youngtify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "user")
public class ESUser {
    @Id
    private String id;
    private String fullname;
    private String phonenumber;
    private String email;
    private String avatar;
    private String dateOfBirth;
    private int gender;
}
