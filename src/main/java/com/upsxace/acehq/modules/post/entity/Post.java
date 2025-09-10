package com.upsxace.acehq.modules.post.entity;

import com.upsxace.acehq.modules.common.AuditableEntity;
import com.upsxace.acehq.modules.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Post extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String text;

    @Column
    private Long likesCount;

    @Column
    private Long commentsCount;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @PrePersist
    public void defaults(){
        if (likesCount == null) likesCount = 0L;
        if (commentsCount == null) commentsCount = 0L;
    }
}
