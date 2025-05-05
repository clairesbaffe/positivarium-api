package com.positivarium.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    // CORS config to authorise specific origins
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF deactivation, required for JWT
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS config application
                .authorizeHttpRequests(authorize -> authorize
                        // Banned users cannot perform sensitive actions (admin, publisher)
                        .requestMatchers(HttpMethod.POST, "/api/admin/**", "/api/publisher/**").not().hasRole("BAN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**", "/api/publisher/**").not().hasRole("BAN")
                        .requestMatchers(HttpMethod.PATCH, "/api/admin/**", "/api/publisher/**").not().hasRole("BAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**", "/api/publisher/**").not().hasRole("BAN")

                        // Banned user can still unfollow and cancel publisher request
                        .requestMatchers(HttpMethod.POST, "/api/user/follow/**", "/api/user/publisher_request/").not().hasRole("BAN")

                        // Banned users cannot post or delete comments
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").not().hasRole("BAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").not().hasRole("BAN")

                        // Banned users cannot like but they can unlike
                        .requestMatchers(HttpMethod.POST, "/api/likes/article/*").not().hasRole("BAN")

                        // Banned users cannot report articles or comments
                        .requestMatchers(HttpMethod.POST, "/api/reports/articles/*", "/api/reports/comments/*").not().hasRole("BAN")

                        // Banned users cannot delete articles
                        .requestMatchers(HttpMethod.DELETE, "/api/articles/*").not().hasRole("BAN")

                        // Access control based on roles
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/publisher/**").hasRole("PUBLISHER")
                        .requestMatchers("/api/user/**", "/api/articles/followed").hasRole("USER")
                        .requestMatchers("/api/journal/**", "/api/global_preferences/**").hasRole("USER")


                        .requestMatchers(HttpMethod.DELETE,"/api/articles/").hasAnyRole("ADMIN", "PUBLISHER")
                        .requestMatchers(HttpMethod.GET, "/api/articles/followed").hasRole("USER")

                        // Public access to certain routes (homepage, registration, login)
                        .requestMatchers("/", "/index", "/test", "/test/*", "/api/register", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/articles/", "/api/articles/*", "/api/articles/published/*", "/api/articles/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/comments/", "api/comments/article/*", "api/comments/user/*", "api/comments/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/profile/publisher/*").permitAll()

                        // Any other request needs authentication
                        .anyRequest().authenticated()
                )
                // Adding JWT filter, verifying user token and role
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

