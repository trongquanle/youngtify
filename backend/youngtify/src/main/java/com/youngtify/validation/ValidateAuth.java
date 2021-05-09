package com.youngtify.validation;

import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.model.AuthRequest;
import com.youngtify.message.ServiceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidateAuth {
    public static ServiceResult validateUser(AuthRequest authRequest){
        String errorCode = null;
        ServiceResult result = new ServiceResult();
        List<String> messages = new ArrayList<>();
        Pattern regexEmail = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");
        Pattern regexPwd = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)");
        if(authRequest.getUsername() == null || authRequest.getUsername().isEmpty()){
            messages.add(MessageConstant.USERNAME_NULL);
            errorCode = ErrorConstant.USERNAME_NULL;
        }else if (!regexEmail.matcher(authRequest.getUsername()).matches()){
            messages.add(MessageConstant.USERNAME_INVALID);
            errorCode = ErrorConstant.USERNAME_INVALID;
        }
        if (authRequest.getPassword() == null || authRequest.getPassword().isEmpty()){
            messages.add(MessageConstant.PASSWORD_NULL);
            errorCode = errorCode != null ? errorCode : ErrorConstant.PASSWORD_NULL;
        }else if (authRequest.getPassword().length() < 8){
            messages.add(MessageConstant.PASSWORD_MIN);
            errorCode = errorCode != null ? errorCode : ErrorConstant.PASSWORD_MIN;
        }else if (authRequest.getPassword().length() > 25){
            messages.add(MessageConstant.PASSWORD_MAX);
            errorCode = errorCode != null ? errorCode : ErrorConstant.PASSWORD_MAX;
        }else if (!regexPwd.matcher(authRequest.getPassword()).find()){
            messages.add(MessageConstant.PASSWORD_INVALID);
            errorCode = errorCode != null ? errorCode : ErrorConstant.PASSWORD_INVALID;
        }
        result.setMessage(MessageConstant.INVALID);
        result.setCode(errorCode);
        result.setErrors(messages);
        return result;
    }
}
