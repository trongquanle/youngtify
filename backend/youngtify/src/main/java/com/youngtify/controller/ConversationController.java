package com.youngtify.controller;

import com.youngtify.service.IConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @Autowired
    private IConversationService conversationService;

    /**
     * Lấy danh sách phòng chat
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getConversations(){
        return ResponseEntity.ok(conversationService.getConversation().getData());
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getConversationById(@PathVariable UUID userId){
        logger.info("getConversationById: " + userId.toString());
        return ResponseEntity.ok(conversationService.getConversationById(userId).getData());
    }

    /**
     * Xóa phòng chat
     * @param conversationId
     * @return
     */
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable UUID conversationId){
        logger.info("deleteConversation: " + conversationId.toString());
        return ResponseEntity.ok(conversationService.deleteConversation(conversationId).getData());
    }
}
