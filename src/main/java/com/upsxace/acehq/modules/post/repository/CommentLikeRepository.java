package com.upsxace.acehq.modules.post.repository;

import com.upsxace.acehq.modules.post.entity.CommentLike;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {
    @Query("SELECT cl FROM CommentLike cl WHERE cl.comment.post.id = :postId AND cl.comment.id = :commentId AND cl.profileId = :profileId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CommentLike> findByPostIdCommentIdAndProfileIdForUpdate(UUID postId, UUID commentId, UUID profileId);
}
