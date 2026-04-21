package com.project.hospitalsystem.Service.Implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.hospitalsystem.Entity.Department;
import com.project.hospitalsystem.Entity.Doctor;
import com.project.hospitalsystem.Mapper.HospitalMapper;
import com.project.hospitalsystem.Model.DoctorRequest;
import com.project.hospitalsystem.Model.DoctorResponse;
import com.project.hospitalsystem.Model.DoctorSummaryModel;
import com.project.hospitalsystem.Repo.DepartmentRepository;
import com.project.hospitalsystem.Repo.DoctorRepository;
import com.project.hospitalsystem.Service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final HospitalMapper hospitalMapper;

    @Override
    @Transactional
    public DoctorResponse registerDoctor(DoctorRequest request) {
        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new IllegalStateException("License number already exists!" + request.getLicenseNumber());
        }
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists!" + request.getEmail());
        }

        Long deptId = Long.parseLong(request.getDepartmentId());
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new IllegalStateException("Department not found with id: " + deptId));

        Doctor doctor = Doctor.builder()
                .name(request.getName())
                .gender(request.getGender())
                .specialization(request.getSpecialization())
                .dateOfBirth(request.getDateOfBirth())
                .licenseNumber(request.getLicenseNumber())
                .phoneNumber(request.getPhoneNumber())
                .bloodGroup(request.getBloodGroup())
                .email(request.getEmail())
                .qualification(request.getQualification())
                .consultationFee(request.getConsultationFee())
                .department(department)
                .build();
        if (doctor.getEmail() == null || doctor.getLicenseNumber() == null) {
            throw new IllegalArgumentException("Critical Doctor data is missing before save.");
        }
        Doctor saveDoctor = doctorRepository.save(doctor);

        return hospitalMapper.mapDoctorResponse(saveDoctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
        if (doctor == null) {
            throw new IllegalStateException("Unexpected null entity returned from repository");
        }
        return hospitalMapper.mapDoctorResponse(doctor);
    }

    @Override
    public List<DoctorSummaryModel> getAllDoctorsForPatients() {
        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getDepartment() != null && doctor.getDepartment().isActive())
                .map(hospitalMapper::mapDoctorSummary)
                .toList();
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(hospitalMapper::mapDoctorResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorSummaryModel> getAllDoctorsSummary() {
        return doctorRepository.findAll().stream()
                .map(hospitalMapper::mapDoctorSummary)
                .collect(Collectors.toList());
    }
}
