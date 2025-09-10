package com.upsxace.acehq.modules.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post_likes")
@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private UUID postId;

    @Column
    private UUID profileId;

    @Column
    @CreationTimestamp
    private LocalDateTime likedAt;
}
