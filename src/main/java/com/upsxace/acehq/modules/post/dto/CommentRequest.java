package com.upsxace.acehq.modules.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Comment content is required.")
    @Size(min = 2, message = "The comment content is too small.")
    @Size(max = 300, message = "The comment content must be at most 300 characters.")
    private final String text;
}