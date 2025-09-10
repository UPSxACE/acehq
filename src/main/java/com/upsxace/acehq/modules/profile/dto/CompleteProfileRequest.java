package com.upsxace.acehq.modules.profile.dto;

import com.upsxace.acehq.modules.profile.validation.SafeURL;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompleteProfileRequest {
    @NotNull(message = "Username is required.")
    @Size(min = 2, max = 32,  message = "Username must contain between 2 and 32 characters.")
    @Pattern(regexp = "^[A-Za-z0-9_-]*$", message = "Username can only contain letters, numbers and '_' or '-'.")
    private final String username;
    @Size(min = 2, max = 100, message = "Name must contain between 2 and 100 characters.")
    private final String name;
    @SafeURL
    private final String avatar;
}
