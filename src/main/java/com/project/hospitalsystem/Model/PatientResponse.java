package com.project.hospitalsystem.Model;

import java.time.LocalDate;

import com.project.hospitalsystem.BloodGenderType.BloodGroupType;
import com.project.hospitalsystem.BloodGenderType.GenderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {
    // Patient personal details
    private Long id;
    private String name;
    private String phonenumber;
    private String email;
    private LocalDate dateofbirth;
    private BloodGroupType bloodgroup;
    private GenderType gender;

    // Patient address details
    private String fullAddress;
    private String city;
    private String pincode;

    // Insurance details ( if available)
    private String insuranceProvider;
    private String policyNumber;
    private boolean isInsuranceActive;
}
