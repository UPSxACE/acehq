package com.upsxace.acehq.modules.profile.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Profile {
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
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private LocalDateTime bannedAt;

    @Column
    private LocalDateTime bannedUntil;

    @Column
    private String bannedReason;
}
