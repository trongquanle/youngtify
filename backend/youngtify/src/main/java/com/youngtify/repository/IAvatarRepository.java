package com.youngtify.repository;

import com.youngtify.entity.AvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IAvatarRepository extends JpaRepository<AvatarEntity, UUID> {

    @Modifying
    @Query("UPDATE AvatarEntity SET isUsing = :status WHERE userId = :userId")
    void updateByUserId(int status, UUID userId);

    void deleteAllByUserId(UUID userId);
}
