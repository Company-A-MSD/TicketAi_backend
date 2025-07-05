package com.backend.ticketai_backend.config;

import com.backend.ticketai_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/employees/login/**", "/", "/api/register", "/api/login").permitAll()
            .requestMatchers("/api/users/**", "/api/delete_user/**", "/api/employees/**", "/api/tickets/all").hasRole("ADMIN")
            .requestMatchers("/api/tickets/assigned", "/api/notifications/on-ticket-update").hasRole("EMPLOYEE")
            .requestMatchers("/api/tickets", "/api/delete_account", "/api/notifications").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/api/tickets/{id}").hasAnyRole("USER", "EMPLOYEE", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/api/tickets/{id}").hasAnyRole("EMPLOYEE", "ADMIN")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Option 1: For development - allow specific origins
        config.addAllowedOrigin("http://localhost:3000"); // Add your frontend URL
        config.addAllowedOrigin("http://localhost:8080"); // Add other allowed origins
        
        // Option 2: For production - be more restrictive
        // config.addAllowedOrigin("https://yourdomain.com");
        
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}