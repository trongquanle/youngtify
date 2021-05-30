package com.youngtify.repository;

import com.youngtify.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IConversationRepository extends JpaRepository<ConversationEntity, UUID> {
}
