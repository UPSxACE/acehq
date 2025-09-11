package com.upsxace.acehq.modules.post.repository;

import com.upsxace.acehq.modules.post.entity.Comment;
import com.upsxace.acehq.modules.post.entity.CommentDetailed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Optional<Comment> findByPostIdAndIdAndPostDeletedAtIsNullAndDeletedAtIsNull(UUID postId, UUID id);

    @Query("""
            SELECT new com.upsxace.acehq.modules.post.entity.CommentDetailed(
                c,
                CASE WHEN (SELECT COUNT(cl) FROM CommentLike cl WHERE cl.commentId = c.id AND cl.profileId = :profileId) > 0 THEN TRUE ELSE FALSE END
            ) FROM Comment c WHERE c.post.id = :postId AND c.id = :id AND c.post.deletedAt IS NULL AND c.deletedAt IS NULL
            """)
    Optional<CommentDetailed> findByPostIdAndIdAndProfileIdAndPostDeletedAtIsNullAndDeletedAtIsNullWithDetails(UUID postId, UUID id, UUID profileId);
}
