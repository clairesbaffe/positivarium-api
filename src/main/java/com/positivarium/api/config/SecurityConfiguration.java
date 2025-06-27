package com.positivarium.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Allows PreAuthorize annotations in controllers
public class SecurityConfiguration {

    // CORS allowed origins injection. This makes the app more flexible
    // Because CORS are not the same depending on environment
    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    // Password encoder using bcrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JWT authentication entry manager
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    // JWT auth filter for requests
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenProvider tokenProvider,
            UserDetailsService customUserDetailsService
    ) {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    // CORS config to authorise specific origins
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Central authentication manager
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Security filters chain configuration
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtTokenProvider tokenProvider,
            UserDetailsService customUserDetailsService
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF deactivation, required for JWT
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS config application
                .authorizeHttpRequests(authorize -> authorize
                        // BAN restrictions are set in controllers via annotation @PreAuthorize("!hasRole('BAN')")

                        // Banned users cannot perform sensitive actions (admin, publisher)
                        // Banned user can still unfollow and cancel publisher request
                        // Banned users cannot post or delete comments
                        // Banned users cannot like, but they can unlike
                        // Banned users cannot report articles or comments
                        // Banned users cannot delete articles

                        // Access control based on roles
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/publisher/**").hasRole("PUBLISHER")
                        .requestMatchers("/api/user/**", "/api/articles/followed").hasRole("USER")
                        .requestMatchers("/api/journal/**", "/api/global_preferences/**").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/articles/followed").hasRole("USER")

                        // Public access to certain routes (homepage, registration, login)
                        .requestMatchers("/", "/index", "/test", "/test/*", "/api/register", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/articles/", "/api/articles/*", "/api/articles/published/*", "/api/articles/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/comments/", "/api/comments/article/*", "/api/comments/user/*", "/api/comments/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/profile/publisher/*").permitAll()

                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Any other request needs authentication
                        .anyRequest().authenticated()
                )
                // Adding JWT filter, verifying user token and role
                .addFilterBefore(jwtAuthenticationFilter(tokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

