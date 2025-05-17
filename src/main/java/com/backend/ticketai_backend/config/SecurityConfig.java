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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/employees/login/**","/","/api/register","/api/login").permitAll()
        .requestMatchers("/api/employees/**","/api/tickets/all","/api/users/**","/api/delete_user").hasRole("ADMIN")
        .requestMatchers("/api/tickets/assigned","/api/notifications/on-ticket-update").hasRole("EMPLOYEE")
        .requestMatchers("/api/tickets","/api/delete_account","/api/notifications").hasRole("USER")
        .requestMatchers(HttpMethod.GET,"/api/tickets/:ticketId").hasAnyRole("USER","EMPLOYEE","ADMIN")
        .requestMatchers(HttpMethod.PATCH,"/api/tickets/:ticketId").hasAnyRole("EMPLOYEE","ADMIN")
        .anyRequest().authenticated()
    )
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}