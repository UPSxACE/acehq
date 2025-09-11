package com.upsxace.acehq.modules.post.repository;

import com.upsxace.acehq.modules.post.entity.Comment;
import com.upsxace.acehq.modules.post.entity.CommentDetailed;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @EntityGraph(attributePaths = {"profile"})
    Optional<Comment> findByPostIdAndIdAndPostDeletedAtIsNullAndDeletedAtIsNull(UUID postId, UUID id);

    @Query("""
            SELECT new com.upsxace.acehq.modules.post.entity.CommentDetailed(
                c,
                CASE WHEN (SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.id = c.id AND cl.profileId = :profileId) > 0 THEN TRUE ELSE FALSE END
            ) FROM Comment c JOIN FETCH c.profile WHERE c.post.id = :postId AND c.id = :id AND c.post.deletedAt IS NULL AND c.deletedAt IS NULL
            """)
    Optional<CommentDetailed> findByPostIdAndIdAndProfileIdAndPostDeletedAtIsNullAndDeletedAtIsNullWithDetails(UUID postId, UUID id, UUID profileId);

    @Query("""
            SELECT c FROM Comment c JOIN FETCH c.profile WHERE c.post.id = :postId AND c.id = :id AND c.post.deletedAt IS NULL AND c.deletedAt IS NULL
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Comment> findByPostIdAndIdAndPostDeletedAtIsNullAndDeletedAtIsNullForUpdate(UUID postId, UUID id);

    @EntityGraph(attributePaths = {"profile"})
    List<Comment> findAllByPostIdAndPostDeletedAtIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(UUID postId);

    @Query("""
            SELECT new com.upsxace.acehq.modules.post.entity.CommentDetailed(
                c,
                CASE WHEN (SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.id = c.id AND cl.profileId = :profileId) > 0 THEN TRUE ELSE FALSE END
            ) FROM Comment c JOIN FETCH c.profile WHERE c.post.id = :postId AND c.post.deletedAt IS NULL AND c.deletedAt IS NULL ORDER BY c.createdAt DESC
            """)
    List<CommentDetailed> findAllByPostIdAndProfileIdAndPostDeletedAtIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(UUID postId, UUID profileId);
}
