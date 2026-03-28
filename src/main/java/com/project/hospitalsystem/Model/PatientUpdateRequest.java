package com.project.hospitalsystem.Model;

import com.project.hospitalsystem.BloodGenderType.BloodGroupType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatientUpdateRequest {

    @NotBlank(message = "Patient can not be empty")
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name should only contain letters")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String phoneNumber;

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

}
