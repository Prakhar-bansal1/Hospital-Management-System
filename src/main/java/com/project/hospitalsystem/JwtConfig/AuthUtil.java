package com.project.hospitalsystem.JwtConfig;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.project.hospitalsystem.Entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthUtil {

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

    public String getUserIdFromToken(String token) {
       String claims= Jwts.parser()
        .verifyWith(getSecurityKey())
         .build()
         .parseSignedClaims(token)
         .getPayload()
         .getSubject();
        return claims;
    }
   

    

}
