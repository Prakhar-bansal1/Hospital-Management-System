package com.project.hospitalsystem.Model;

import java.time.LocalDate;

import com.project.hospitalsystem.EnumType.BloodGroupType;
import com.project.hospitalsystem.EnumType.GenderType;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
public class DoctorRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private GenderType gender;

    @NotBlank
    @Size(max = 100)
    private String specialization;

    @NotNull
    @Past(message = "Date is invalid")
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(max = 10)
    private String licenseNumber;

    @NotBlank
    @Size(max = 10)
    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;

    @NotNull(message = "Patient blood group is required for emergencies")
    private BloodGroupType bloodGroup;

    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 255)
    private String qualification;

    @NotNull
    private Double consultationFee;

    @NotNull
    private String departmentId;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 14, message = "Password must be between 8 and 14 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain one uppercase, one lowercase, one number, and one special character")
    private String password;
}
