package com.project.hospitalsystem.Security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Entity.Role;
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
    @Autowired
    AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    public LoginResponseModel login(LoginRequestModel loginRequestModel) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestModel.getEmail(), loginRequestModel.getPassword()));
        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAcceessToken(user);
        return new LoginResponseModel(token, user.getId());
    }

    public SignupResponseModel signup(SignupRequestModel signupRequestModel) {
        User user = userRepository.findByEmail(signupRequestModel.getEmail()).orElse(null);
        if (signupRequestModel.getEmail() == null || signupRequestModel.getPassword() == null) {
            throw new IllegalArgumentException("Email and Password cannot be null");
        }
        if (user != null)
            throw new IllegalArgumentException("User already exists");

        User newUser = User.builder()
                .email(signupRequestModel.getEmail())
                .password(signupRequestModel.getPassword())
                .roles(Set.of(Role.PATIENT))
                .isActive(true)
                .build();
        // need check----------> fix done
        if (newUser == null) {
            return null;
        }
        User savedUser = userRepository.save(newUser);
        return new SignupResponseModel(savedUser.getEmail(), savedUser.getId());
    }
}
