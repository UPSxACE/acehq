package com.upsxace.acehq.modules.post.repository;

import com.upsxace.acehq.modules.post.entity.PostLike;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    @Query("SELECT pl FROM PostLike pl WHERE pl.postId = :postId AND pl.profileId = :profileId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PostLike> findByPostIdAndProfileIdForUpdate(UUID postId, UUID profileId);
}
