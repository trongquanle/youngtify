package com.youngtify.service;

import com.youngtify.entity.FriendEntity;
import com.youngtify.message.BaseResponse;
import com.youngtify.model.Friend;
import com.youngtify.model.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface IFriendService {
    /**
     * Gửi yêu cầu/xác nhận/hủy yêu cầu kết bạn kết bạn
     * @return
     */
    BaseResponse requestFriend(UserRequest requestDTO);

    /**
     * Hủy kết bạn/block
     * @return
     */
    BaseResponse changeStatusFriend(FriendEntity friend);

    /**
     * Lấy danh sách yếu cầu kết bạn
     * @return
     */
    BaseResponse getFriends(int status);

    /**
     * Lấy danh sách chờ xác nhận kết bạn
     * @return
     */
    BaseResponse getFriendRequests(String mKey);

}
