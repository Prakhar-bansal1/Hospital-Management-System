package com.project.hospitalsystem.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.hospitalsystem.Entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthUtil {
    @Value("${jwt.secretkey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

public String generateAcceessToken(User user){
    return Jwts.builder()
    .subject(user.getEmail())
    .claim("userId", user.getId().toString())
    .issuedAt(new Date())
    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 50))
    .signWith(getSecretKey())
    .compact();
}
}
