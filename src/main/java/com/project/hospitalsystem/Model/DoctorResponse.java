package com.project.hospitalsystem.Model;

import java.time.LocalDate;

import com.project.hospitalsystem.EnumType.BloodGroupType;
import com.project.hospitalsystem.EnumType.GenderType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
    private BloodGroupType bloodGroup;
    private GenderType gender;
    private String specialization;
    private String licenseNumber;

    private Long departmentId;
    private String departmentName;

    private LocalDate joiningDate;
}
