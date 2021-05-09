package com.youngtify.service;

import com.youngtify.message.BaseResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public interface IMessageService {
    BaseResponse getMessage(UUID roomId, String key);
    BaseResponse getMessageImages(UUID roomId);
}
