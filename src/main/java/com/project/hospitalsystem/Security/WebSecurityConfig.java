package com.project.hospitalsystem.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/sys/secureline/admin/**").hasRole("ADMIN")
                        .requestMatchers("/sys/internal/reception/**").hasAnyRole("RECEPTION", "ADMIN")
                        .requestMatchers("/sys/doctor/**").hasRole("DOCTOR")
                        .requestMatchers("/sys/patient/**").hasRole("PATIENT")
                        .requestMatchers("/hospital/**").permitAll()
                        .anyRequest().authenticated());
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
