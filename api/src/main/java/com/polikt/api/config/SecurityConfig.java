package com.polikt.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define the public GET routes
    private static final String[] PUBLIC_GET_ROUTES = {
            "/courses",
            "/courses/*",
            "/courses/*/*",
            "/courses/*/modules",
            "/courses/*/modules/*",
            "/courses/*/modules/*/content",
            "/courses/*/modules/*/content/*",

            "/agencies",
            "/agencies/*",

            "/news",
            "/news/*",

            "/guides",
            "/guides/*"
    };

    private final JwtAuthFilter jwtAuthFilter;

    // Inject the JWT Auth Filter
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ROUTES).permitAll()

                // Login endpoint
                .requestMatchers(HttpMethod.POST, "/users/auth", "/users").permitAll()

                // Entrypoint
                .requestMatchers("/").permitAll()

                // All the other endpoints require a valid JWT
                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
