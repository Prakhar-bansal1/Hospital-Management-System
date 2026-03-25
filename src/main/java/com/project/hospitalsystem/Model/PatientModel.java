package com.project.hospitalsystem.Model;
import java.time.LocalDate;

import com.project.hospitalsystem.BloodType.BloodGroupType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "password")

public class PatientModel {

    @NotNull(message ="Patient name is mandatory")
    private String name;

    @NotNull(message ="Patient gender is mandatory")  // Application-level check                  // Database-level constraint
    private String gender;

    @Email(message = "Invalid email format")  // Format validation
    private String email;
    
    @NotNull
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    // Regex (Regular Expressions) is a tool to define patterns for string matching.
    private String phonenumber;

    @NotNull(message =" Patient date of birth is mandatory")
    private LocalDate dateofbirth;

    @NotNull(message = "Patient blood group is required for emergencies")
    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @NotNull(message ="Patient address is mandatory")
    private String address;

    @NotNull
    private String password;
}


