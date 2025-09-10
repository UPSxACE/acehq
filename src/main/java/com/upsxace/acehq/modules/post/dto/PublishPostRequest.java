package com.upsxace.acehq.modules.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PublishPostRequest {
    @NotBlank(message = "Post content is required.")
    @Size(min = 2, message = "The post content is too small.")
    @Size(max = 500, message = "The post content must be at most 500 characters.")
    private final String text;
}
