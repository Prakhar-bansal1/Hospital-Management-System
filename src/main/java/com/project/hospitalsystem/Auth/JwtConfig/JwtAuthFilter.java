package com.project.hospitalsystem.Auth.JwtConfig;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.Repo.UserRepository;
import com.project.hospitalsystem.Auth.JwtConfig.RefreshToken.RefreshTokenRepository;
import com.project.hospitalsystem.Auth.JwtConfig.RefreshToken.RefreshTokenService;
import com.project.hospitalsystem.Auth.JwtConfig.RefreshToken.RefreshToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtUtil jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = requestTokenHeader.split("Bearer ")[1];
        
        try {
            String userId = jwtService.getUserIdFromToken(token);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findById(Long.parseLong(userId)).orElseThrow();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            // Token is invalid or expired, try to refresh it
            if (jwtService.isTokenExpired(token)) {
                String userId = extractUserIdFromExpiredToken(token);
                if (userId != null) {
                    User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
                    if (user != null) {
                        RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
                        if (refreshToken != null && !refreshTokenService.isExpired(refreshToken)) {
                            String newAccessToken = jwtService.generateAccessToken(user);
                            response.setHeader("X-New-Access-Token", newAccessToken);
                            
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractUserIdFromExpiredToken(String token) {
        try {
            javax.crypto.SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("default123"));
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

}