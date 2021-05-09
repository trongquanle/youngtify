package com.youngtify.repository;

import com.youngtify.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface FriendRepository extends JpaRepository<FriendEntity, UUID>,
        JpaSpecificationExecutor<FriendEntity> {
}
