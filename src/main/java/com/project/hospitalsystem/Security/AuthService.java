package com.project.hospitalsystem.Security;

import java.security.AuthProvider;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Entity.Role;
import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.EnumType.AuthProvidertype;
import com.project.hospitalsystem.Model.LoginRequestModel;
import com.project.hospitalsystem.Model.LoginResponseModel;
import com.project.hospitalsystem.Model.SignupRequestModel;
import com.project.hospitalsystem.Model.SignupResponseModel;
import com.project.hospitalsystem.Repo.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

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

    public User signupInternal(LoginRequestModel signupRequestModel, AuthProvidertype authproviderType, String providerId) {
        User user = userRepository.findByEmail(signupRequestModel.getEmail()).orElse(null);
        if (signupRequestModel.getEmail() == null || signupRequestModel.getPassword() == null) {
            throw new IllegalArgumentException("Email and Password cannot be null");
        }
        if (user != null)
            throw new IllegalArgumentException("User already exists");

        User newUser = User.builder()
                .email(signupRequestModel.getEmail())
                .providerId(providerId)
                .provider(authproviderType)
                .roles(Set.of(Role.PATIENT))
                .isActive(true)
                .build();
                if(authproviderType == AuthProvidertype.EMAIL){
                    newUser.setPassword(signupRequestModel.getPassword());
                }
        if (newUser == null) {
            return null;
        }
        return userRepository.save(newUser);
    }

    public SignupResponseModel signup(SignupRequestModel signupRequestModel) {
       User user = signupInternal(signupRequestModel, AuthProvidertype.EMAIL, null);
       return new SignupResponseModel(user.getEmail(), user.getId());
    }

    public ResponseEntity<LoginResponseModel> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        AuthProvidertype providerType = authUtil.getProvidertypefromRegistrationId(registrationId);
       String providerId = authUtil.determineproviderIdFromOAuth2User(oAuth2User, providerType);
    User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
        
    String email = (String) oAuth2User.getAttribute("email");
    User emailuser = userRepository.findByEmail(email).orElse(null);
    if(user==null && emailuser==null){
        String username = authUtil.determineUsernameFromOAuth2User(oAuth2User,registrationId, providerId);
    SignupRequestModel signupRequestModel = signupInternal(new LoginRequestModel(username,null), AuthProvidertype.OAUTH2);
    signup(signupRequestModel);
    }
    else if(user!=null){
        if(email!=null&& !email.equals(user.getEmail())){
            user.setEmail(email);
            userRepository.save(user);
        }
    }
    else{
        throw new BadCredentialsException("Email already associated with another account"+ email);
    }
    LoginResponseModel loginResponseModel = new LoginResponseModel(authUtil.generateAcceessToken(user), user.getId());
return ResponseEntity.ok(loginResponseModel);
}
}
