package com.project.hospitalsystem.Security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.Model.LoginRequestModel;
import com.project.hospitalsystem.Model.LoginResponseModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
private final AuthenticationManager authenticationManager;
private final AuthUtil authUtil;
    public LoginResponseModel login(LoginRequestModel loginRequestModel) {
       Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequestModel.getEmail(), loginRequestModel.getPassword())
        
    
    );
    User user= (User) authentication.getPrincipal();
    String token= authUtil.generateAcceessToken(user);
    return new LoginResponseModel(token, user.getId());
       
    }
    
}// its till login now see vipul for concept
