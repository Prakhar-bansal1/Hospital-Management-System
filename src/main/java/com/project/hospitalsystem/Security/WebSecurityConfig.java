package com.project.hospitalsystem.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccesslHandler oAuth2SuccesslHandler;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter, OAuth2SuccesslHandler oAuth2SuccesslHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.oAuth2SuccesslHandler = oAuth2SuccesslHandler;                             
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(
                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/sys/secureline/admin/**").hasRole("ADMIN")
                        .requestMatchers("/sys/internal/reception/**").hasAnyRole("RECEPTION", "ADMIN")
                        .requestMatchers("/sys/doctor/**").hasRole("DOCTOR")
                        .requestMatchers("/sys/patient/**").hasRole("PATIENT")
                        .requestMatchers("/hospital/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oAuth2 -> oAuth2.failureHandler((request, response, exception) -> {
                    log.error("OAuth2 login failed: {}", exception.getMessage());
                })
                        .successHandler(oAuth2SuccesslHandler)
            );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
