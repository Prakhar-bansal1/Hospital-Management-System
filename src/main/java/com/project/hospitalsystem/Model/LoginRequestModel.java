package com.project.hospitalsystem.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestModel {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Security Policy: Password must be at least 6 characters")
    private String password;
}
