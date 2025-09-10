package com.upsxace.acehq.modules.post.repository;

import com.upsxace.acehq.modules.post.entity.Post;
import com.upsxace.acehq.modules.post.entity.PostDetailed;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

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
    Optional<PostDetailed> findByIdAndDeletedAtIsNullWithDetails(UUID id, UUID profileId);


    @Query("SELECT p FROM Post p JOIN FETCH p.profile WHERE p.id = :id AND p.deletedAt IS NULL")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Post> findByIdAndDeletedAtIsNullForUpdate(UUID id);
}
