package com.project.hospitalsystem.Auth.JwtConfig.RefreshToken;

import java.time.Instant;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private static final Long REFRESH_TOKEN_VALIDITY = (long) (12 * 60 * 60 * 1000);

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .expiry(Instant.now().plusMillis(REFRESH_TOKEN_VALIDITY))
                .user(user)
                .build();
        if (refreshToken == null) {
            return null;
        }
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getOrCreateRefreshToken(User user) {
        RefreshToken existingToken = refreshTokenRepository.findByUser(user);

        if (existingToken != null && existingToken.getExpiry().compareTo(Instant.now()) > 0) {
            return existingToken;
        }

        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }

        return createRefreshToken(user);
    }

    public RefreshToken verifyAndRefreshToken(@NonNull String refreshTokenVerify) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenVerify)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (refreshToken.getExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Token expired");
        }

        return refreshToken;
    }

    public boolean isExpired(RefreshToken refreshToken) {
        return refreshToken != null && refreshToken.getExpiry().compareTo(Instant.now()) < 0;
    }

}
