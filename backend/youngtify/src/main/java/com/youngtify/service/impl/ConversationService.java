package com.youngtify.service.impl;

import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.constant.StoreConstant;
import com.youngtify.entity.ParticipantEntity;
import com.youngtify.message.BaseResponse;
import com.youngtify.message.ServiceResult;
import com.youngtify.model.Conversation;
import com.youngtify.model.CustomUser;
import com.youngtify.repository.UserRequestRepository;
import com.youngtify.service.IConversationService;
import com.youngtify.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ConversationService extends BaseService implements IConversationService {

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Override
    public BaseResponse getConversation() {
        BaseResponse response = new BaseResponse();
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Conversation> conversations = this.getAll(
                    Conversation.class,
                    StoreConstant.Proc_GetParticipantsByUserId,
                    customUser.getUserId().toString());
            response.setSuccess(true);
            response.setData(conversations);
        }catch (Exception e){
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
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    @Override
    public BaseResponse getConversationById(UUID userId) {
        BaseResponse response = new BaseResponse();
        try{
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<ParticipantEntity> participants = this.getAll(
                    ParticipantEntity.class,
                    StoreConstant.Proc_GetConversationByUserId,
                    customUser.getUserId().toString(),
                    userId.toString());
            response.setSuccess(true);
            if (participants.size() > 0){
                Map<String, UUID> map = new HashMap<>();
                map.put("roomId", participants.get(0).getConversationId());
                response.setData(map);
            }
            else response.setData(null);
        }catch (Exception e){
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }
}
