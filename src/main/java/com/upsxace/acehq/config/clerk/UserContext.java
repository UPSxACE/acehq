package com.upsxace.acehq.config.clerk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upsxace.acehq.modules.profile.entity.UserRole;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class UserContext {
    private final UUID id;
    @JsonIgnore
    @NonNull
    private final String clerkId;
    private final List<GrantedAuthority> authorities;

    @Nullable
    public String getAuthoritiesString(){
        if(authorities == null) return null;
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }

    public boolean isAdmin(){
        if(authorities == null) return false;
        return authorities
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + UserRole.ADMIN));
    }
}
