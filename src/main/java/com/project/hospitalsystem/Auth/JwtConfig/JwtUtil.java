package com.project.hospitalsystem.Auth.JwtConfig;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.project.hospitalsystem.Entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtUtil {

    private static final String jwtSecretKey = "default123";

    private SecretKey getSecurityKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
        .subject(user.getId().toString())
        .claim("roles", user.getRoles().stream().map(role -> role.name()).toList())
        .signWith(getSecurityKey())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000*60*10))//10 minutes
        .signWith(getSecurityKey())
        .compact();
    }

    public String generateRefreshToken(User user){
        return Jwts.builder()
        .subject(user.getId().toString())
        .claim("roles", user.getRoles().stream().map(role -> role.name()).toList())
        .signWith(getSecurityKey())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000*60*60*12))//12 hours
        .signWith(getSecurityKey())
        .compact();
    }

    public String getUserIdFromToken(String token) {
       String claims= Jwts.parser()
        .verifyWith(getSecurityKey())
         .build()
         .parseSignedClaims(token)
         .getPayload()
         .getSubject();
        return claims;
    }

    public boolean isTokenExpired(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSecurityKey())
                .build()
                .parseSignedClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
