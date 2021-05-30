package com.youngtify.service.impl;

import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.constant.StoreConstant;
import com.youngtify.entity.ConversationEntity;
import com.youngtify.entity.MessageEntity;
import com.youngtify.entity.ParticipantEntity;
import com.youngtify.message.BaseResponse;
import com.youngtify.message.ServiceResult;
import com.youngtify.model.Conversation;
import com.youngtify.model.CustomUser;
import com.youngtify.repository.IConversationRepository;
import com.youngtify.repository.UserRequestRepository;
import com.youngtify.service.IConversationService;
import com.youngtify.specification.UserSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConversationService extends BaseService implements IConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationService.class);

    @Override
    public BaseResponse getConversation() {
        BaseResponse response = new BaseResponse();
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info("getConversation: " + customUser.getUserId().toString());
            List<Conversation> conversations = this.getAll(
                    Conversation.class,
                    StoreConstant.Proc_GetParticipantsByUserId,
                    customUser.getUserId().toString());
            response.setData(conversations);
        }catch (Exception e){
            logger.warn("getConversation exception: " + e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    @Override
    public BaseResponse deleteConversation(UUID conversationId) {
        BaseResponse response = new BaseResponse();
        try{
            executeNoneQuery(StoreConstant.Proc_DeleteConversationById, conversationId.toString());
            response.setSuccess(true);
            response.setData(new ServiceResult(ErrorConstant.DELETE_CONVERSATION_SUCCESSFULLY, MessageConstant.DELETE_CONVERSATION_SUCCESSFULLY));
        }catch (Exception e){
            logger.warn("deleteConversation exception: " + e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    @Override
    public BaseResponse getConversationById(UUID userId) {
        BaseResponse response = new BaseResponse();
        try{
            logger.info("getConversationById: " + userId.toString());
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<ParticipantEntity> participants = this.getAll(
                    ParticipantEntity.class,
                    StoreConstant.Proc_GetConversationByUserId,
                    customUser.getUserId().toString(),
                    userId.toString());
            Map<String, UUID> map = new HashMap<>();
            if (participants.size() > 0){
                map.put("roomId", participants.get(0).getConversationId());
            }else {
                // Thêm mới room vào csdl
                UUID converId = UUID.randomUUID();
                executeNoneQuery(StoreConstant.Proc_InsertConversation,
                        converId.toString(),
                        customUser.getUserId().toString(),
                        userId.toString());
                map.put("roomId", converId);
            }
            response.setSuccess(true);
            response.setData(map);
        }catch (Exception e){
            logger.warn("getConversationById exception: " + e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }
}
