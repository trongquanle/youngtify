package com.youngtify.repository;

import com.youngtify.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IMessageRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findAllByConversationIdAndMessageTypeOrderByCreatedDateDesc(UUID roomId, int msgType);
}
