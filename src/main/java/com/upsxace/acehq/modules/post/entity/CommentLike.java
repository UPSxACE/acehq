package com.upsxace.acehq.modules.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment_likes")
@Getter @Setter
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private UUID commentId;

    @Column
    private UUID profileId;

    @Column
    @CreationTimestamp
    private LocalDateTime likedAt;
}
