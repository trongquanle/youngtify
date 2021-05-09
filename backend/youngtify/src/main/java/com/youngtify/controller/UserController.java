package com.youngtify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngtify.entity.ProfileEntity;
import com.youngtify.message.BaseResponse;
import com.youngtify.model.CustomUser;
import com.youngtify.model.PasswordRequest;
import com.youngtify.model.UserProfile;
import com.youngtify.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping(value = "/api/users")
    public ResponseEntity<?> getProfile(){
        CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetailsService.findById(customUser.getUserId()));
    }

    @GetMapping(value = "/api/users/{userId}")
    public ResponseEntity<?> getProfileById(@PathVariable UUID userId){
        return ResponseEntity.ok(userDetailsService.findById(userId));
    }

    @PutMapping(value = "/api/users")
    public ResponseEntity<?> editProfile(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam(value = "profile") String profile,
                                         @RequestParam(value = "type") int type) throws JsonProcessingException {
        UserProfile userProfile = new ObjectMapper().readValue(profile, UserProfile.class);
        BaseResponse responseService = userDetailsService.editProfile(file, userProfile, type);
        HttpStatus httpStatus = HttpStatus.OK;
        if (!responseService.isSuccess())
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus).body(responseService.getData());
    }

    @PostMapping(value = "/api/users/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest passwordRequest){
        return ResponseEntity.ok(userDetailsService.changePassword(passwordRequest.getPassword()).getData());
    }
}
