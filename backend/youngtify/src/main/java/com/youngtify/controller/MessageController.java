package com.youngtify.controller;

import com.youngtify.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getMessages(@PathVariable UUID roomId, @RequestParam(value = "key", required = false) String key) throws ParseException {
        return ResponseEntity.ok(messageService.getMessage(roomId, key).getData());
    }

    @GetMapping("/{roomId}/images")
    public ResponseEntity<?> getMessageImages(@PathVariable UUID roomId){
        return ResponseEntity.ok(messageService.getMessageImages(roomId).getData());
    }

}
