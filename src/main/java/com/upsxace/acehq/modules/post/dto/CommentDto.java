package com.upsxace.acehq.modules.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.upsxace.acehq.modules.profile.dto.ProfileDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentDto {
    private final UUID id;
    private final String text;
    private final Integer likesCount;
    private final LocalDateTime createdAt;
    private final ProfileDto profile;
    // details
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean liked;
}
