package com.youngtify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngtify.config.TokenUtil;
import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.entity.UserEntity;
import com.youngtify.model.AuthRequest;
import com.youngtify.model.AuthResponse;
import com.youngtify.exception.BadRequestException;
import com.youngtify.message.BaseResponse;
import com.youngtify.message.ServiceResult;
import com.youngtify.model.UserProfile;
import com.youngtify.repository.UserRepository;
import com.youngtify.service.impl.CustomUserDetailsService;
import com.youngtify.validation.ValidateAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        try {
            logger.info(authRequest.getUsername());
            ServiceResult validateResult = ValidateAuth.validateUser(authRequest);
            if (validateResult.getCode() != null){
                return ResponseEntity.ok(validateResult);
            }
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()){
                UserProfile userProfile = userDetailsService.findByUsername(authRequest.getUsername());
                if (userProfile == null)
                    return ResponseEntity.ok().body(new ServiceResult(ErrorConstant.USERNAME_NOT_EXISTS, MessageConstant.USERNAME_NOT_EXISTS, null));
                String token = tokenUtil.generateToken(userProfile);
                return ResponseEntity.ok(new AuthResponse(token));
            }else
                throw new BadRequestException("Đăng nhập thất bại");
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok().body(new ServiceResult(ErrorConstant.PASSWORD_INCORRECT, MessageConstant.PASSWORD_INCORRECT, null));
        }
    }

    @PostMapping("/external")
    public ResponseEntity<?> externalLogin(@RequestBody UserProfile userProfile){
        try{
            UserProfile user = userDetailsService.findByUsername(userProfile.getUsername());
            if (user != null){
                String token = tokenUtil.generateToken(user);
                return ResponseEntity.ok(new AuthResponse(token));
            }
            if (userProfile.getEmail() != null){
                user = userDetailsService.findByUsername(userProfile.getEmail());
                if (user != null){
                    String token = tokenUtil.generateToken(user);
                    return ResponseEntity.ok(new AuthResponse(token));
                }
            }
            // Thêm mới
            userProfile.setId(UUID.randomUUID());
            BaseResponse response = userDetailsService.externalLogin(userProfile);
            if (response.isSuccess()){
                String token = tokenUtil.generateToken(userProfile);
                return ResponseEntity.ok(new AuthResponse(token));
            }else return ResponseEntity.ok().body(response.getData());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER, null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserProfile user) {
//        ServiceResult validateResult = ValidateAuth.validateUser(authRequest);
//        if (validateResult.getCode() != null){
//            return ResponseEntity.ok(validateResult);
//        }
        BaseResponse response = userDetailsService.save(user);
        HttpStatus httpStatus = HttpStatus.CREATED;
        if (!response.isSuccess())
            httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response.getData());
    }
}
