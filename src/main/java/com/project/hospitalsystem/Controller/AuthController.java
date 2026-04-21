package com.project.hospitalsystem.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalsystem.Model.LoginRequestModel;
import com.project.hospitalsystem.Model.LoginResponseModel;
import com.project.hospitalsystem.Security.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseModel> login(@RequestBody LoginRequestModel loginRequestModel) {
    return ResponseEntity.ok(authService.login(loginRequestModel));
    }

    
}
