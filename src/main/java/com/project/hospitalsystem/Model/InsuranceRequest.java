package com.project.hospitalsystem.Model;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InsuranceRequest {

    @NotBlank(message = "Policy number is required")
    @Size(min = 5,max = 50,message ="Invalid policy number")
    @Pattern(regexp = "^[A-Z0-9-]+$" , message = "Policy number must be Alphanumeric")
    private String policyNumber;

    @NotBlank(message = "Insurance provider is required")
    @Size(max = 100, message = "Invalid insurance provider name")
    private String insuranceProvider;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Can't register expired insurance")
    private LocalDate expiryDate;
}
