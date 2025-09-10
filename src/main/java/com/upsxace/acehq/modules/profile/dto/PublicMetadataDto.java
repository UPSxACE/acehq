package com.upsxace.acehq.modules.profile.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PublicMetadataDto {
    private final UUID id;
    private final String authorities;

    public Map<String, Object> toMap(){
        return Map.of(
                "id", this.id,
                "authorities", this.authorities
        );
    }
}
