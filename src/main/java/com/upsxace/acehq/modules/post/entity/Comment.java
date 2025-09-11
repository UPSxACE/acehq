package com.upsxace.acehq.modules.post.entity;

import com.upsxace.acehq.modules.core.AuditableEntity;
import com.upsxace.acehq.modules.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Comment extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String text;

    @Column
    private Long likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @PrePersist
    public void defaults(){
        if (likesCount == null) likesCount = 0L;
    }
}
