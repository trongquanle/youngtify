package com.youngtify.controller;

import com.youngtify.service.IConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

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
        return ResponseEntity.ok(conversationService.getConversationById(userId).getData());
    }

    /**
     * Xóa phòng chat
     * @param conversationId
     * @return
     */
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable UUID conversationId){
        return ResponseEntity.ok(conversationService.deleteConversation(conversationId).getData());
    }
}
