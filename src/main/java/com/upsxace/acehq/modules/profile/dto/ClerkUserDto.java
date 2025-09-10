package com.upsxace.acehq.modules.profile.dto;


import com.upsxace.acehq.modules.profile.entity.Profile;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ClerkUserDto {
    private final String username;
    private final String email;

    public static ClerkUserDto fromProfile(Profile profile){
        return new ClerkUserDto(profile.getUsername(), profile.getEmail());
    }
}
