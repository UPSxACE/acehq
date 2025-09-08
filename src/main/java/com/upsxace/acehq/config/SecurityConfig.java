package com.upsxace.acehq.config;

import com.upsxace.acehq.config.clerk.ClerkAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${app.frontend-url")
    private String frontendUrl;

    @Bean
    @Order(-1)
    @Profile("!prod")
    public SecurityFilterChain developmentSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                );

        return http.build();
    }

    @Bean
    @Order(0)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, ClerkAuthenticationFilter clerkAuthenticationFilter) throws Exception {
        http.cors(corsConfig -> corsConfig.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList(frontendUrl));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setMaxAge(3600L * 24); // tells the browser to remember these configurations for 24h
            return config;
        }));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(clerkAuthenticationFilter, SecurityContextHolderFilter.class);


        http.authorizeHttpRequests(registry -> registry
                .anyRequest().authenticated()
        );

        http.exceptionHandling(c -> {
            c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)); // return 401 when requiring authentication
            c.accessDeniedHandler(((request, response, accessDeniedException) ->
                    response.setStatus(HttpStatus.FORBIDDEN.value())) // return 403 on access denied
            );
        });

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        // we utilize Clerk to authenticate and authorize, instead of UserDetailsService.
        return username -> { throw new IllegalStateException("UserDetailsService bean is disabled."); };
    }

}
