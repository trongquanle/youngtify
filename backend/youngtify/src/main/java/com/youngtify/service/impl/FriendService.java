package com.youngtify.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngtify.config.ElasticsearchConfig;
import com.youngtify.constant.ErrorConstant;
import com.youngtify.constant.MessageConstant;
import com.youngtify.constant.StoreConstant;
import com.youngtify.model.*;
import com.youngtify.entity.FriendEntity;
import com.youngtify.entity.UserRequestEntity;
import com.youngtify.enumration.FriendStatus;
import com.youngtify.enumration.UserRequestStatus;
import com.youngtify.message.BaseResponse;
import com.youngtify.message.ServiceResult;
import com.youngtify.repository.FriendRepository;
import com.youngtify.repository.UserRequestRepository;
import com.youngtify.service.IFriendService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FriendService extends BaseService implements IFriendService {

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private ElasticsearchConfig elasticsearchConfig;

    @Override
    public BaseResponse requestFriend(UserRequest requestDTO) {
        BaseResponse response = new BaseResponse();
        String errorCode = null;
        String messageCode = null;
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (requestDTO.getSenderId() == null)
                requestDTO.setSenderId(customUser.getUserId());
            else if (requestDTO.getReceiverId() == null)
                requestDTO.setReceiverId(customUser.getUserId());
            UserRequestEntity userRequestEntity = this.firstOrDefault(
                    UserRequestEntity.class,
                    StoreConstant.Proc_GetUserRequest,
                    requestDTO.getSenderId().toString(),
                    requestDTO.getReceiverId().toString()
            );
            switch (UserRequestStatus.getStatus(requestDTO.getStatus())) {
                case REQUEST:
                    if (userRequestEntity != null) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_REQUEST, MessageConstant.INVALID_FRIEND_REQUEST, null));
                        return response;
                    } else if (!requestDTO.getSenderId().equals(customUser.getUserId())) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_REQUEST, MessageConstant.INVALID_FRIEND_REQUEST, null));
                        return response;
                    }
                    int temp = this.executeNoneQuery(
                            StoreConstant.Proc_InsertFriendRequest,
                            requestDTO.getSenderId().toString(),
                            requestDTO.getReceiverId().toString(),
                            requestDTO.getStatus()
                    );
                    errorCode = ErrorConstant.ADD_FRIEND_REQUEST;
                    messageCode = MessageConstant.ADD_FRIEND_REQUEST;
                    response.setSuccess(true);
                    break;
                case ACCEPT:
                    if (userRequestEntity == null) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_REQUEST, MessageConstant.INVALID_FRIEND_REQUEST, null));
                        return response;
                    } else if (!userRequestEntity.getReceiverId().equals(customUser.getUserId())) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_USER_RECEIVER, MessageConstant.INVALID_USER_RECEIVER, null));
                        return response;
                    }
                    this.executeNoneQuery(
                            StoreConstant.Proc_InsertFriendRequest,
                            requestDTO.getSenderId().toString(),
                            requestDTO.getReceiverId().toString(),
                            requestDTO.getStatus()
                    );
                    errorCode = ErrorConstant.ACCEPT_FRIEND_REQUEST;
                    messageCode = MessageConstant.ACCEPT_FRIEND_REQUEST;
                    response.setSuccess(true);
                    break;
                default:
                    if (userRequestEntity == null || UserRequestStatus.getStatus(userRequestEntity.getStatus()) == UserRequestStatus.ACCEPT) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_REQUEST, MessageConstant.INVALID_FRIEND_REQUEST, null));
                        return response;
                    }
                    this.executeNoneQuery(
                            StoreConstant.Proc_InsertFriendRequest,
                            requestDTO.getSenderId().toString(),
                            requestDTO.getReceiverId().toString(),
                            requestDTO.getStatus()
                    );
                    errorCode = ErrorConstant.CANCEL_FRIEND_REQUEST;
                    messageCode = MessageConstant.CANCEL_FRIEND_REQUEST;
                    response.setSuccess(true);
                    break;
            }
            response.setData(new ServiceResult(errorCode, messageCode));
        } catch (Exception e) {
            response.setSuccess(false);
            errorCode = ErrorConstant.INTERNAL_SERVER;
            messageCode = MessageConstant.INTERNAL_SERVER;
            response.setData(new ServiceResult(errorCode, messageCode));
        }
        return response;
    }

    @Override
    public BaseResponse changeStatusFriend(FriendEntity friend) {
        BaseResponse response = new BaseResponse();
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (friend.getUserId() == null)
                friend.setUserId(customUser.getUserId());
            else
                friend.setFriendId(customUser.getUserId());
            FriendEntity friendEntity = this.firstOrDefault(
                    FriendEntity.class,
                    StoreConstant.Proc_GetFriendById,
                    friend.getUserId().toString(),
                    friend.getFriendId().toString()
            );
            if (friendEntity == null) {
                response.setData(new ServiceResult(ErrorConstant.USER_FRIEND_NOT_EXISTS, MessageConstant.USER_FRIEND_NOT_EXISTS));
                return response;
            }
            switch (friendEntity.getStatus()) {
                case BLOCK:
                    if (friendEntity.getStatus() != FriendStatus.FRIEND) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_STATUS, MessageConstant.INVALID_FRIEND_STATUS));
                        break;
                    }
                    this.executeNoneQuery(
                            StoreConstant.Proc_ChangeStatusFriend,
                            friendEntity.getUserId().toString(),
                            friendEntity.getFriendId().toString(),
                            customUser.getUserId(),
                            friend.getStatus().getValue()
                    );
                    response.setSuccess(true);
                    response.setData(new ServiceResult(ErrorConstant.BLOCK_SUCCESSFULLY, MessageConstant.BLOCK_SUCCESSFULLY));
                    break;
                case UNFIREND:
                    if (friendEntity.getStatus() != FriendStatus.FRIEND) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_STATUS, MessageConstant.INVALID_FRIEND_STATUS));
                        break;
                    }
                    this.executeNoneQuery(
                            StoreConstant.Proc_ChangeStatusFriend,
                            friendEntity.getUserId().toString(),
                            friendEntity.getFriendId().toString(),
                            customUser.getUserId(),
                            friend.getStatus().getValue()
                    );
                    response.setSuccess(true);
                    response.setData(new ServiceResult(ErrorConstant.UNFRIEND_SUCCESSFULLY, MessageConstant.UNFRIEND_SUCCESSFULLY));
                    break;
                case UNBLOCK:
                    if (friendEntity.getStatus() != FriendStatus.BLOCK) {
                        response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_STATUS, MessageConstant.INVALID_FRIEND_STATUS));
                        break;
                    }
                    this.executeNoneQuery(
                            StoreConstant.Proc_ChangeStatusFriend,
                            friendEntity.getUserId().toString(),
                            friendEntity.getFriendId().toString(),
                            customUser.getUserId(),
                            friend.getStatus().getValue()
                    );
                    response.setSuccess(true);
                    response.setData(new ServiceResult(ErrorConstant.UNBLOCK_SUCCESSFULLY, MessageConstant.UNBLOCK_SUCCESSFULLY));
                    break;
                default:
                    response.setData(new ServiceResult(ErrorConstant.INVALID_FRIEND_STATUS, MessageConstant.INVALID_FRIEND_STATUS));
                    break;
            }
        } catch (Exception e) {
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    @Override
    public BaseResponse getFriends(int status) {
        BaseResponse response = new BaseResponse();
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Friend> friendIds = this.getAll(
                    Friend.class,
                    StoreConstant.Proc_GetListFriend,
                    customUser.getUserId().toString(),
                    status
            );
            if (friendIds.size() > 0){
                List<String> ids = new ArrayList<>();
                friendIds.forEach(friend -> {
                    ids.add(friend.getId());
                });
                QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                        .filter(QueryBuilders.termsQuery("_id", ids));
                String[] includeFields = new String[] {"id", "fullname", "avatar"};
                SearchHit[] searchHits = elasticsearchConfig.search(queryBuilder, includeFields);
                List<Friend> friends = new ArrayList<>();
                for (SearchHit hit:searchHits){
                    Friend friend = new Friend();
                    Map<String, Object> map = hit.getSourceAsMap();
                    friend.setId(hit.getId());
                    if (map.containsKey("fullname"))
                        friend.setFullname(map.get("fullname").toString());
                    if (map.containsKey("avatar"))
                        friend.setAvatarUrl(map.get("avatar").toString());
                    friends.add(friend);
                }
                response.setData(friends);
            }else response.setData(null);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    @Override
    public BaseResponse getFriendRequests(String mKey) {
        BaseResponse response = new BaseResponse();
        try {
            CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = null;
            if (mKey != null)
                date = formatter.parse(mKey);
            List<FriendSender> userRequests = this.getAll(
                    FriendSender.class,
                    StoreConstant.Proc_GetListFriendRequest,
                    customUser.getUserId().toString(),
                    date
            );
            if (userRequests.size() > 0){
                Date modifiedDate = userRequests.get(userRequests.size()-1).getModifiedDate();
                mKey = modifiedDate.toString().substring(0, 19);
                FriendSenderRes res = new FriendSenderRes();
                res.setKey(mKey);
                List<String> ids = new ArrayList<>();
                userRequests.forEach(item -> {
                    ids.add(item.getSenderId());
                });
                QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                        .filter(QueryBuilders.termsQuery("_id", ids));
                SearchHit[] searchHits = elasticsearchConfig.search(queryBuilder);
                List<ESUserRes> esUserRes = new ArrayList<>();
                ObjectMapper mapper = new ObjectMapper();
                for (SearchHit hit:searchHits){
                    String id = hit.getId();
                    if (!customUser.getUserId().toString().equals(id)){
                        esUserRes.add(mapper.convertValue(hit.getSourceAsMap(), ESUserRes.class));
                    }
                }
                res.setItems(esUserRes);
                response.setData(res);
            }else response.setData(null);
            response.setSuccess(true);
        }catch (Exception e){
            response.setData(new ServiceResult(ErrorConstant.INTERNAL_SERVER, MessageConstant.INTERNAL_SERVER));
        }
        return response;
    }

    private BaseResponse blockFriend(BaseResponse response, Friend friendDTO) {
        return response;
    }

    private BaseResponse unFriend(BaseResponse response, com.youngtify.model.Friend friendDTO) {
        return response;
    }
}
