package com.youngtify.exception;

import com.youngtify.constant.ErrorConstant;
import com.youngtify.message.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceResult usernameNotFoundException(UsernameNotFoundException ex, WebRequest request){
        return new ServiceResult(ErrorConstant.USERNAME_NOT_EXISTS, ex.getMessage(), null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ServiceResult resourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        return new ServiceResult("404", ex.getMessage(), null);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceResult internalServerErrorException(InternalServerErrorException ex, WebRequest request){
        return new ServiceResult("500", ex.getMessage(), null);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ServiceResult badRequestException(BadRequestException ex, WebRequest request){
        return new ServiceResult("400", ex.getMessage(), null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ServiceResult unauthorizedException(UnauthorizedException ex, WebRequest request){
        return new ServiceResult("401", ex.getMessage(), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ServiceResult forbiddenException(ForbiddenException ex, WebRequest request){
        return new ServiceResult("403", ex.getMessage(), null);
    }
}
