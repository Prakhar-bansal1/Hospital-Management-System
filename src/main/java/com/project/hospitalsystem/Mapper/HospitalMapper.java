package com.project.hospitalsystem.Mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.project.hospitalsystem.Entity.Doctor;
import com.project.hospitalsystem.Entity.Patient;
import com.project.hospitalsystem.Model.DoctorResponse;
import com.project.hospitalsystem.Model.DoctorSummaryModel;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;

import jakarta.validation.constraints.NotNull;

@Component
@Validated
public class HospitalMapper {
    public DoctorResponse mapDoctorResponse(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        @NotNull
        Long entityId = doctor.getId();
        if (entityId == null) {
            throw new IllegalStateException("Doctor ID cannot be null");
        }
        Long safeId = entityId.longValue();
        return DoctorResponse.builder()
                .id(safeId)
                .name(doctor.getName())
                .gender(doctor.getGender())
                .specialization(doctor.getSpecialization())
                .dateOfBirth(doctor.getDateOfBirth())
                .licenseNumber(doctor.getLicenseNumber())
                .phoneNumber(doctor.getPhoneNumber())
                .bloodGroup(doctor.getBloodGroup())
                .email(doctor.getEmail())
                .qualification(doctor.getQualification())
                // Ternary Operator to handle null values for department
                .departmentId(doctor.getDepartment() != null ? doctor.getDepartment().getId() : null)
                .departmentName(doctor.getDepartment() != null ? doctor.getDepartment().getDepartmentName()
                        : "Department Not assigned yet!")
                .joiningDate(doctor.getCreatedAt() != null ? doctor.getCreatedAt().toLocalDate() : LocalDate.now())
                .build();
    }

    public PatientResponse mapPatientResponse(Patient patient) {
        if (patient == null)
            throw new IllegalArgumentException("Patient cannot be null");
        return PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .phonenumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .dateofbirth(patient.getDateofbirth())
                .bloodgroup(patient.getBloodGroup())
                .gender(patient.getGender())
                .fullAddress(patient.getFullAddress())
                .city(patient.getCity())
                .pincode(patient.getPincode())
                .insuranceProvider(
                        patient.getInsurance() != null ? patient.getInsurance().getInsuranceProvider() : "Not Insured")
                .policyNumber(patient.getInsurance() != null ? patient.getInsurance().getPolicyNumber() : "Not Insured")
                .isInsuranceActive(patient.getInsurance() != null && patient.getInsurance().getExpiryDate() != null
                        && patient.getInsurance().getExpiryDate().isAfter(LocalDate.now()))
                .build();

    }

    public DoctorSummaryModel mapDoctorSummary(Doctor doctor) {
        if (doctor == null)
            return null;

        return DoctorSummaryModel.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .departmentName(doctor.getDepartment() != null ? doctor.getDepartment().getDepartmentName() : "N/A")
                .qualification(doctor.getQualification())
                .consultationFee(doctor.getConsultationFee())
                .isAvailableToday(true)
                .build();
    }

    public void updatePatientFromRequest(PatientUpdateRequest request, Patient patient) {
        if (request == null || patient == null)
            return;
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setFullAddress(request.getFullAddress());
        patient.setCity(request.getCity());
        patient.setPincode(request.getPincode());
    }
}
