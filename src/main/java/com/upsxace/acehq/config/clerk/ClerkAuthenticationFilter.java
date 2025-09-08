package com.upsxace.acehq.config.clerk;

import com.clerk.backend_api.helpers.security.AuthenticateRequest;
import com.clerk.backend_api.helpers.security.models.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.security.models.RequestState;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ClerkAuthenticationFilter extends OncePerRequestFilter {
    @Value("${clerk.secret}")
    private String clerkSecret;
    @Value("${clerk.authorized-party}")
    private String clerkAuthorizedParty;

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
                .secretKey(clerkSecret)
                .authorizedParty(clerkAuthorizedParty)
                .build());

        if (!requestState.isAuthenticated() || requestState.claims().isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var claims = requestState.claims().get();

        var publicMetadata = claims.get("public_metadata", PublicMetadata.class);
        var id = publicMetadata != null ? publicMetadata.getId() : null;
        var authorities = publicMetadata != null ? AuthorityUtils.commaSeparatedStringToAuthorityList(publicMetadata.getAuthorities()) : null;

        var userContext = new UserContext(id, authorities);

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
