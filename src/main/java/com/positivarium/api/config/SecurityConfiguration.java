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
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/publisher/**", "/api/publisher/**").hasRole("PUBLISHER")
                        .requestMatchers(HttpMethod.POST, "/api/articles/").hasRole("PUBLISHER")
                        .requestMatchers(HttpMethod.PUT, "/api/articles/publish/**").hasRole("PUBLISHER")
                        .requestMatchers(HttpMethod.DELETE,"/articles/").hasAnyRole("ADMIN", "PUBLISHER")
                        .requestMatchers("/user/**").hasRole("USER")
                        // Banned users cannot participate in community
                        .requestMatchers(HttpMethod.POST, "/api/articles/", "/api/comments/*", "/api/likes/article/*", "/api/reports/articles/*", "/api/reports/comments/*").not().hasRole("BAN")
                        .requestMatchers(HttpMethod.PUT, "/api/articles/publish/*").not().hasRole("BAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/articles/*", "/api/comments/*", "api/likes/article/*").not().hasRole("BAN")
                        // Public access to certain routes, such as homepage, registration and login
                        .requestMatchers("/", "/index", "/test", "/test/*", "/api/register", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/articles/", "/api/articles/*", "/api/articles/published/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/comments/", "api/comments/article/*", "api/comments/user/*", "api/comments/*").permitAll()

                        .anyRequest().authenticated() // All other requests need authentication
                )
                // Adding JWT filter, verifying user token and role
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

