package com.youngtify.service.impl;

import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.constant.StoreConstant;
import com.youngtify.entity.MessageEntity;
import com.youngtify.message.BaseResponse;
import com.youngtify.message.ServiceResult;
import com.youngtify.model.Message;
import com.youngtify.repository.IMessageRepository;
import com.youngtify.service.IMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageService extends BaseService implements IMessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private IMessageRepository messageRepository;

    @Override
    public BaseResponse getMessage(UUID roomId, String key) {
        BaseResponse response = new BaseResponse();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = null;
            if (key != null)
                date = formatter.parse(key);
            List<Message> messages = this.getAll(Message.class, StoreConstant.Proc_GetMessages, roomId.toString(), date);
            Collections.reverse(messages);
            response.setSuccess(true);
            response.setData(messages);
        }
        catch (ParseException p){
            logger.warn(p.getMessage());
            response.setData(new ServiceResult(ErrorConstant.DATE_INVALID, MessageConstant.DATE_INVALID));
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    @Override
    public BaseResponse getMessageImages(UUID roomId) {
        BaseResponse response = new BaseResponse();
        try {
            List<MessageEntity> messages = messageRepository.findAllByConversationIdAndMessageTypeOrderByCreatedDateDesc(roomId,1);
            List<String> images = new ArrayList<>();
            if (!messages.isEmpty()){
                messages.forEach(message -> {
                    List<String> imgs = Arrays.asList(message.getMessage().split(","));
                    Collections.reverse(imgs);
                    images.addAll(imgs);
                });
            }
            response.setSuccess(true);
            response.setData(images);
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }
}
