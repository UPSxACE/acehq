package com.upsxace.acehq.config.clerk;

import com.clerk.backend_api.helpers.security.AuthenticateRequest;
import com.clerk.backend_api.helpers.security.models.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.security.models.RequestState;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ClerkAuthenticationFilter extends OncePerRequestFilter {
    @Value("${clerk.secret}")
    private String clerkSecret;
    @Value("${clerk.authorized-party}")
    private String clerkAuthorizedParty;
    @Value("${clerk.jwt-encoded}")
    private String clerkJwtEncoded;

    private String jwtKey;

    @PostConstruct
    private void init() {
        jwtKey = new String(Base64.getDecoder().decode(clerkJwtEncoded));
    }

    private Map<String, List<String>> getHeaders(HttpServletRequest request) {
        var headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return Collections.emptyMap();
        }

        return Collections.list(headerNames).stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> Collections.list(request.getHeaders(name))
                ));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        RequestState requestState = AuthenticateRequest.authenticateRequest(getHeaders(request), AuthenticateRequestOptions
                .jwtKey(jwtKey)
                .authorizedParty(clerkAuthorizedParty)
                .build());

        if (!requestState.isAuthenticated() || requestState.claims().isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var claims = requestState.claims().get();
        var id = Optional.ofNullable(claims.get("id", String.class))
                .map(UUID::fromString)
                .orElse(null);
        var authorities = claims.get("authorities", String.class);

        var userContext = new UserContext(id, claims.getSubject(), AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

        var authentication = new UsernamePasswordAuthenticationToken(
                userContext,
                null,
                userContext.getAuthorities()
        );

        // adds metadata about the request, such as ip address, to the authentication object
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
