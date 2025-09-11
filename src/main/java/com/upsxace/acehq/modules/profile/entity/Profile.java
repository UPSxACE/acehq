package com.upsxace.acehq.modules.profile.entity;

import com.upsxace.acehq.modules.core.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Profile extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String clerkId;

    @Column
    @Enumerated(EnumType.STRING)
    private ProfileType type;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String avatar;

    @Column
    private LocalDateTime bannedAt;

    @Column
    private LocalDateTime bannedUntil;

    @Column
    private String bannedReason;

    @PrePersist
    public void defaults(){
        if (name == null) name = "";
    }
}
