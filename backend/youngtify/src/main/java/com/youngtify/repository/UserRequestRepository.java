package com.youngtify.repository;

import com.youngtify.entity.UserRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface UserRequestRepository extends
        JpaRepository<UserRequestEntity, UUID>,
        JpaSpecificationExecutor<UserRequestEntity> {
}
