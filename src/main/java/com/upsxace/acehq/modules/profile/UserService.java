package com.upsxace.acehq.modules.profile;

import com.upsxace.acehq.config.clerk.UserContext;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Nullable
    public UserContext getUserContext(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return (UserContext) authentication.getPrincipal();
    }
}
