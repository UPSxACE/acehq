package com.upsxace.acehq.modules.post.repository;

import com.upsxace.acehq.modules.post.entity.Post;
import com.upsxace.acehq.modules.post.entity.PostDetailed;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findByIdAndDeletedAtIsNull(UUID id);

    @Query("""
            SELECT new com.upsxace.acehq.modules.post.entity.PostDetailed(
                p,
                CASE WHEN (SELECT COUNT(pl) FROM PostLike pl WHERE pl.postId = :id AND pl.profileId = :profileId) > 0 THEN TRUE ELSE FALSE END
            ) FROM Post p WHERE p.id = :id AND p.deletedAt IS NULL
            """)
    Optional<PostDetailed> findByIdAndProfileIdAndDeletedAtIsNullWithDetails(UUID id, UUID profileId);


    @Query("SELECT p FROM Post p JOIN FETCH p.profile WHERE p.id = :id AND p.deletedAt IS NULL")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Post> findByIdAndDeletedAtIsNullForUpdate(UUID id);

    List<Post> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    @Query("""
            SELECT new com.upsxace.acehq.modules.post.entity.PostDetailed(
                p,
                CASE WHEN (SELECT COUNT(pl) FROM PostLike pl WHERE pl.postId = p.id AND pl.profileId = :profileId) > 0 THEN TRUE ELSE FALSE END
            ) FROM Post p WHERE p.deletedAt IS NULL ORDER BY p.createdAt DESC
            """)
    List<PostDetailed> findAllByProfileIdAndDeletedAtIsNullOrderByCreatedAtDescWithDetails(UUID profileId);

    @Query("SELECT p FROM Post p WHERE p.createdAt > :date AND p.deletedAt IS NULL ORDER BY (p.likesCount + p.commentsCount) DESC")
    List<Post> findPopularPostsAfterDate(LocalDateTime date);

    @Query("""
           SELECT new com.upsxace.acehq.modules.post.entity.PostDetailed(
                p,
                CASE WHEN (SELECT COUNT(pl) FROM PostLike pl WHERE pl.postId = p.id AND pl.profileId = :profileId) > 0 THEN TRUE ELSE FALSE END
           ) FROM Post p WHERE p.createdAt > :date AND p.deletedAt IS NULL ORDER BY (p.likesCount + p.commentsCount) DESC
           """)
    List<PostDetailed> findPopularPostsAfterDateWithDetails(UUID profileId, LocalDateTime date);
}
