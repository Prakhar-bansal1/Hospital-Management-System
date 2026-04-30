package com.project.hospitalsystem.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import com.project.hospitalsystem.Auth.JwtConfig.JwtUtil;
import com.project.hospitalsystem.Auth.JwtConfig.RefreshToken.RefreshToken;
import com.project.hospitalsystem.Auth.JwtConfig.RefreshToken.RefreshTokenRepository;
import com.project.hospitalsystem.Auth.JwtConfig.RefreshToken.RefreshTokenService;
import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.Model.LoginRequestModel;
import com.project.hospitalsystem.Model.LoginResponseModel;
import com.project.hospitalsystem.Model.SignupRequestModel;
import com.project.hospitalsystem.Model.SignupResponseModel;
import com.project.hospitalsystem.Repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponseModel login(LoginRequestModel loginRequestModel) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestModel.getEmail(), loginRequestModel.getPassword()));
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshToken(user);
        return new LoginResponseModel(token, refreshToken.getRefreshToken(), user.getId());
    }

    public SignupResponseModel register(SignupRequestModel signupRequestModel) {
       User user = userRepository.findByPhoneNumber(signupRequestModel.getPhoneNumber()).orElse(null);
        if(user != null){
            throw new RuntimeException("User with this phone number already exists");
        }
        
        User newUser = User.builder()
        .name(signupRequestModel.getName())
        .email(signupRequestModel.getEmail())
        .password(signupRequestModel.getPassword())
        .phoneNumber(signupRequestModel.getPhoneNumber())
        .build();
        if(newUser==null){
            return null;
        }
       userRepository.save(newUser);
       return new SignupResponseModel("User registered successfully", newUser.getId());
    }

    public LoginResponseModel refreshToken(@NonNull String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenService.verifyAndRefreshToken(refreshTokenString);
        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        refreshTokenRepository.delete(refreshToken);
        
        return new LoginResponseModel(newAccessToken, newRefreshToken.getRefreshToken(), user.getId());
    }
}
