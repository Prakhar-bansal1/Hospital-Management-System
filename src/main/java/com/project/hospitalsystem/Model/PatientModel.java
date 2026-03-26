package com.project.hospitalsystem.Model;
import java.time.LocalDate;

import com.project.hospitalsystem.BloodType.BloodGroupType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "password")

public class PatientModel {

    @NotNull(message ="Patient name is mandatory")
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z ]+$") // regex allows only Letters and Spaces in name
    private String name;

    @NotNull(message ="Patient gender is mandatory")  // Application-level check                  
    @Size(max = 10)
    private String gender;

    @NotNull(message = "Email is mandatory")
    @Email(message = "Invalid email format")  // Format validation
    private String email;
    
    @NotNull
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    // Regex (Regular Expressions) is a tool to define patterns for string matching.
    private String phonenumber;

    @NotNull(message =" Patient date of birth is mandatory")
    private LocalDate dateofbirth;

    @NotNull(message = "Patient blood group is required for emergencies")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Use format A+, O-")
    private BloodGroupType bloodGroup;

    @NotNull(message = "Address is mandatory")
    @Size(max = 255)
    private String addressLine1;

    @NotNull(message = "City is mandatory")
    @Size(max = 100)
    private String city;

    @NotNull(message = "Pincode is mandatory")
    @Pattern(regexp = "^[1-9][0-9]$", message = "Invalid Pincode") 
    private String pincode;

    @NotNull(message = "Password is mandatory")
    @Size(min = 6, max = 100)
    private String password;
}