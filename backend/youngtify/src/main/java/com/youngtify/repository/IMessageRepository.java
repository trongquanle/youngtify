package com.youngtify.repository;

import com.youngtify.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface IMessageRepository extends JpaRepository<MessageEntity, UUID>, JpaSpecificationExecutor<MessageEntity> {
    List<MessageEntity> findAllByConversationIdAndMessageTypeOrderByCreatedDateDesc(UUID roomId, int msgType);
}
