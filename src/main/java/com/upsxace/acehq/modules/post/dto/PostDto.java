package com.upsxace.acehq.modules.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.upsxace.acehq.modules.profile.dto.ProfileDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PostDto {
    private final UUID id;
    private final String text;
    private final Integer likesCount;
    private final Integer commentsCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final ProfileDto profile;
    // details
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean liked;
}
