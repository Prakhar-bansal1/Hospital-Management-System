package com.project.hospitalsystem.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.project.hospitalsystem.Entity.Role;
import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.EnumType.AuthProvidertype;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthUtil {

    private String jwtSecretKey = "default123";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAcceessToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRoles().stream().map(Role::name).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 50))
                .signWith(getSecretKey())
                .compact();
    }

    public String getIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public AuthProvidertype getProvidertypefromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProvidertype.GOOGLE;
            case "facebook" -> AuthProvidertype.FACEBOOK;
            case "github" -> AuthProvidertype.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported registration ID: " + registrationId);
        };
    }

    public String determineproviderIdFromOAuth2User(OAuth2User oAuth2User, AuthProvidertype providerType) {

        String providerId = switch (providerType) {
            case GOOGLE -> oAuth2User.getAttribute("sub").toString();
            case FACEBOOK -> oAuth2User.getAttribute("id").toString();
            case GITHUB -> oAuth2User.getAttribute("id").toString();
            default -> {
                log.error("Unsupported provider type: {}", registrationId);
                throw new IllegalArgumentException("Unsupported provider type: " + registrationId);
            }
        };
        if (providerId == null || providerId.isBlank()) {
            log.error("Unable to determine provider id: {}", registrationId);
            throw new IllegalArgumentException("Unable to determine provider id for Login");
        }
        return providerId;
    }

    public String getUsernameFromAuth2User(OAuth2User oAuth2User, AuthProvidertype providerType) {
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            Object name = oAuth2User.getAttribute("name");
            if (name != null) {
                return name.toString();
            }
            throw new IllegalArgumentException("Unable to determine username from OAuth2User");
        }
        return email;
    }
}