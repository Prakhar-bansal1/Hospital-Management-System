package com.project.hospitalsystem.Model;

import java.time.LocalDate;

import com.project.hospitalsystem.BloodGenderType.BloodGroupType;
import com.project.hospitalsystem.BloodGenderType.GenderType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")

public class PatientRequest {

    @NotBlank(message = "Patient name is mandatory")
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name should only contain letters")
    // regex allows only Letters and Spaces in name
    private String name;

    @NotBlank(message = "Patient gender is mandatory")
    private GenderType gender;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    // Regex (Regular Expressions) is a tool to define patterns for string matching.
    private String phoneNumber;

    @NotNull(message = " Patient date of birth is mandatory")
    @Past(message = "Date is invalid")
    private LocalDate dateofbirth;

    @NotNull(message = "Patient blood group is required for emergencies")
    private BloodGroupType bloodGroup;

    @NotBlank(message = "Address is mandatory")
    @Size(max = 255)
    private String fullAddress;

    @NotBlank(message = "City is mandatory")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "Pincode is mandatory")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid Pincode")
    private String pincode;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 14, message = "Password must be between 8 and 14 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain one uppercase, one lowercase, one number, and one special character")
    private String password;

    @Valid
    private InsuranceRequest insurance;
}