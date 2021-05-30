package com.youngtify.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.constant.StoreConstant;
import com.youngtify.entity.AvatarEntity;
import com.youngtify.model.CustomUser;
import com.youngtify.entity.ProfileEntity;
import com.youngtify.entity.UserEntity;
import com.youngtify.exception.UsernameNotFoundException;
import com.youngtify.message.BaseResponse;
import com.youngtify.message.ServiceResult;
import com.youngtify.model.ESUser;
import com.youngtify.model.UserProfile;
import com.youngtify.repository.IAvatarRepository;
import com.youngtify.repository.IESUserRepository;
import com.youngtify.repository.ProfileRepository;
import com.youngtify.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomUserDetailsService extends BaseService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private IESUserRepository esUserRepository;

    @Value("${storate.url}")
    private String storageUrl;

    @Override
    @Transactional
    public CustomUser loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_NOT_EXISTS);
        }
        return new CustomUser(user.getUsername(), user.getPassword(),
                new ArrayList<>(), user.getId());
    }

    @Transactional
    public BaseResponse save(UserProfile user) {
        BaseResponse response = new BaseResponse();
        try {
            UserEntity userEntity = userRepository.findByUsername(user.getUsername());
            if (userEntity != null) {
                response.setSuccess(false);
                ServiceResult serviceResult = new ServiceResult(ErrorConstant.USERNAME_ALREADY_EXISTS, MessageConstant.USERNAME_ALREADY_EXISTS, null);
                response.setData(serviceResult);
            } else {
                user.setPassword(bcryptEncoder.encode(user.getPassword()));
//                if (user.getAvatarUrl() == null)
//                    user.setAvatarUrl("/images/profile-7.jpg");
                user.setId(UUID.randomUUID());
                int temp = this.executeNoneQuery(
                        StoreConstant.Proc_InserUserProfile,
                        user.getId().toString(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getPassword(),
                        1,
                        user.getDateOfBirth(),
                        user.getGender(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAvatarUrl()
                );
                if (temp <= 0) throw new Exception();
                saveToElasticsearch(user);
                response.setSuccess(true);
                response.setData(new ServiceResult(ErrorConstant.REGISTER_SUCCES, MessageConstant.REGISTER_SUCCES));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    public BaseResponse externalLogin(UserProfile user){
        BaseResponse response = new BaseResponse();
        try{
            user.setPassword(bcryptEncoder.encode(user.getUsername()));
//                if (user.getAvatarUrl() == null)
//                    user.setAvatarUrl("/images/profile-7.jpg");
            int temp = this.executeNoneQuery(
                    StoreConstant.Proc_InserUserProfile,
                    user.getId().toString(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    1,
                    user.getDateOfBirth(),
                    user.getGender(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAvatarUrl()
            );
            if (temp <= 0) throw new Exception();
            saveToElasticsearch(user);
            response.setSuccess(true);
            response.setData(new ServiceResult(ErrorConstant.REGISTER_SUCCES, MessageConstant.REGISTER_SUCCES));
        }catch (Exception e){
            logger.warn(e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    private void saveToElasticsearch(UserProfile userProfile) {
        try {
            String fullname = null;
            if (userProfile.getFirstName() != null && userProfile.getLastName() != null)
                fullname = (userProfile.getLastName().trim() + " " + userProfile.getFirstName().trim()).trim();
            else if (userProfile.getFirstName() != null)
                fullname = userProfile.getLastName().trim();
            else if (userProfile.getLastName() != null) fullname = userProfile.getFirstName().trim();
            ESUser user = new ESUser(
                    userProfile.getId().toString(),
                    fullname,
                    userProfile.getPhoneNumber(),
                    userProfile.getEmail(),
                    userProfile.getAvatarUrl(),
                    null,
                    0
            );
            esUserRepository.save(user);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public UserProfile findByUsername(String username) {
        return this.firstOrDefault(
                UserProfile.class,
                StoreConstant.Proc_GetUserProfileByUsername,
                username
        );
    }

    public UserProfile findById(UUID userId) {
        return this.firstOrDefault(
                UserProfile.class,
                StoreConstant.Proc_GetUserProfileById,
                userId.toString()
        );
    }

    @Transactional
    public BaseResponse editProfile(MultipartFile file, UserProfile model, int type) {
        BaseResponse response = new BaseResponse();
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ProfileEntity profile = profileRepository.findById(customUser.getUserId()).orElse(null);
            if (profile == null) {
                response.setData(new ServiceResult(ErrorConstant.USERNAME_NOT_EXISTS, MessageConstant.USERNAME_NOT_EXISTS));
                return response;
            }
            String res;
            if (file != null) {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("multipart/form-data");
                RequestBody body = new MultipartBuilder().type(mediaType)
                        .addFormDataPart("file", file.getOriginalFilename(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        file.getBytes()))
                        .build();
                Request request = new Request.Builder()
                        .url(storageUrl)
                        .method("POST", body)
                        .build();
                Response response1 = client.newCall(request).execute();
                Map<String, String> map = new ObjectMapper().readValue(response1.body().string(), Map.class);
                model.setAvatarUrl(map.get("fileName"));
            }
            ESUser esUser = esUserRepository.findById(customUser.getUserId().toString()).orElse(null);
            switch (type) {
                case 1:
                    profile.setDateOfBirth(model.getDateOfBirth());
                    profile.setFirstName(model.getFirstName());
                    profile.setLastName(model.getLastName());
                    profile.setGender(model.getGender());
                    profile.setPhoneNumber(model.getPhoneNumber());
                    if(esUser != null){
                        if (profile.getDateOfBirth() == null) esUser.setDateOfBirth(null);
                        else esUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(model.getDateOfBirth()));
                        esUser.setFullname(model.getLastName() + " " + model.getFirstName());
                        esUser.setGender(model.getGender());
                        esUser.setPhonenumber(model.getPhoneNumber());
                    }
                    break;
                case 2:
                    profile.setPhoneNumber(model.getPhoneNumber());
                    if (esUser != null) esUser.setPhonenumber(model.getPhoneNumber());
                    break;
            }
            if (esUser == null){
                esUser = new ESUser();
                esUser.setId(profile.getId().toString());
                esUser.setEmail(customUser.getUsername());
                esUser.setFullname(profile.getLastName() + " " + profile.getFirstName());
                esUser.setAvatar(model.getAvatarUrl());
                if (profile.getDateOfBirth() == null) esUser.setDateOfBirth(null);
                else esUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(model.getDateOfBirth()));
                esUser.setGender(profile.getGender());
                esUser.setPhonenumber(profile.getPhoneNumber());
            }
            if (model.getAvatarUrl() != null){
                String avatarId = UUID.randomUUID().toString();
                this.executeNoneQuery(StoreConstant.Proc_InsertAvatar,
                        customUser.getUserId().toString(),
                        model.getAvatarUrl(),
                        avatarId);
                esUser.setAvatar(model.getAvatarUrl());
            }
            profileRepository.save(profile);
            esUserRepository.save(esUser);
            response.setSuccess(true);

            response.setData(new ServiceResult(ErrorConstant.ADD_PROFILE_SUCCES, MessageConstant.ADD_PROFILE_SUCCES));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    public BaseResponse changePassword(String newPassword) {
        BaseResponse response = new BaseResponse();
        try {
            String newPasswordHash = bcryptEncoder.encode(newPassword);
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            this.executeNoneQuery(
                    StoreConstant.Proc_ChangePassword,
                    customUser.getUserId().toString(),
                    newPasswordHash
            );

        } catch (Exception e) {
            logger.warn(e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    public BaseResponse changeProfile() {
        return null;
    }
}
