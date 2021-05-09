package com.youngtify.controller;

import com.youngtify.entity.FriendEntity;
import com.youngtify.model.UserRequest;
import com.youngtify.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private IFriendService friendService;

    /**
     * Kết bạn, hủy kết bạn
     * @param requestDTO
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> requestFriend(@RequestBody UserRequest requestDTO){
        return ResponseEntity.ok(friendService.requestFriend(requestDTO).getData());
    }

    /**
     * Block, hủy block, hủy kết bạn
     * @param friend
     * @return
     */
    @PostMapping("/relationship")
    public ResponseEntity<?> changeStatusFriend(@RequestBody FriendEntity friend ){
        return ResponseEntity.ok(friendService.changeStatusFriend(friend));
    }

    /**
     * Lấy danh sách bạn bè
     * @param status
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getFriends(@RequestParam("status") int status){
        return ResponseEntity.ok(friendService.getFriends(status).getData());
    }

    /**
     * Lấy danh sách lời mời kết bạn
     * @return
     */
    @GetMapping("/request")
    public ResponseEntity<?> getFriendRequests(@RequestParam(value = "key", required = false) String key){
        return ResponseEntity.ok(friendService.getFriendRequests(key).getData());
    }
}
