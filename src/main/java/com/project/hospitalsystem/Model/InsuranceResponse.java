package com.project.hospitalsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceResponse {
    private Long id;
    private String policyNumber;
    private String insuranceProvider;
    private boolean isActive;
    private String expiryDate;
    private String statusMessage;
}
