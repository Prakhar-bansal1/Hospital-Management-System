package com.project.hospitalsystem.Auth.JwtConfig.RefreshToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    RefreshToken findByUser(User user);
}
