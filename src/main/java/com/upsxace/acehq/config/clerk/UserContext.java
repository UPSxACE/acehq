package com.upsxace.acehq.config.clerk;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserContext {
    private final String id;
    private final List<GrantedAuthority> authorities;
}
