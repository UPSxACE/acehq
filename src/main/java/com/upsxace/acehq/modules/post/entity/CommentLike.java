package com.upsxace.acehq.modules.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comment_likes")
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private UUID profileId;

    @Column
    @CreationTimestamp
    private LocalDateTime likedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
