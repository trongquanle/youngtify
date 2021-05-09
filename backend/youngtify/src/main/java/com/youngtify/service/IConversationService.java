package com.youngtify.service;

import com.youngtify.message.BaseResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface IConversationService {

    BaseResponse getConversation();

    BaseResponse deleteConversation(UUID conversationId);
    BaseResponse getConversationById(UUID userId);

}
