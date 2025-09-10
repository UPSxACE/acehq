package com.upsxace.acehq.modules.profile.dto;

import com.upsxace.acehq.modules.profile.entity.ProfileType;
import lombok.Data;

import java.util.UUID;

@Data
public class ProfileDto {
    private UUID id;
    private ProfileType type;
    private String username;
    private String name;
    private String avatar;
}
