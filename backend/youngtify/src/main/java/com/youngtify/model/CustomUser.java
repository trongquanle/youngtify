package com.youngtify.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class CustomUser extends User {

    private UUID userId;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UUID userId) {
        super(username, password, authorities);
        this.userId = userId;
    }
}
