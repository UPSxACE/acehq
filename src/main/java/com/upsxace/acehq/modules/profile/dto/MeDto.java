package com.upsxace.acehq.modules.profile.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class MeDto {
    private final UUID id;
    private final String authorities;
    private final ProfileDto profile;
}
