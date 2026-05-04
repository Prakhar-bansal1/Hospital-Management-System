package com.project.hospitalsystem.Service.Implementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.hospitalsystem.Entity.Department;
import com.project.hospitalsystem.Entity.Doctor;
import com.project.hospitalsystem.Exception.BaseException;
import com.project.hospitalsystem.Exception.ErrorCode;
import com.project.hospitalsystem.Mapper.HospitalMapper;
import com.project.hospitalsystem.Model.DoctorRequest;
import com.project.hospitalsystem.Model.DoctorResponse;
import com.project.hospitalsystem.Model.DoctorSummaryModel;
import com.project.hospitalsystem.Model.PasswordResetRequest;
import com.project.hospitalsystem.Model.PasswordResetResponse;
import com.project.hospitalsystem.Repo.AppointmentRepository;
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
    private final AppointmentRepository appointmentRepository;
    private final HospitalMapper hospitalMapper;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_APPOINTMENTS_PER_SLOT = 10;

    private boolean hasSlotsAvailableToday(Long doctorId) {
        // Check first available slot today (09:30 AM is first slot)
        LocalTime firstSlot = LocalTime.of(9, 30);
        long appointmentsAtFirstSlot = appointmentRepository.countActiveAppointments(doctorId, LocalDate.now(),
                firstSlot);
        return appointmentsAtFirstSlot < MAX_APPOINTMENTS_PER_SLOT;
    }

    @Override
    @Transactional
    public DoctorResponse registerDoctor(DoctorRequest request) {
        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BaseException(ErrorCode.DOCTOR_LICENSE_EXISTS,
                    "License number already exists: " + request.getLicenseNumber());
        }
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new BaseException(ErrorCode.DOCTOR_EMAIL_EXISTS,
                    "Doctor email already exists: " + request.getEmail());
        }

        Long deptId = Long.parseLong(request.getDepartmentId());
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new BaseException(ErrorCode.DEPARTMENT_NOT_FOUND,
                        "Department not found with id: " + deptId));

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
            .password(passwordEncoder.encode(request.getPassword()))
                .department(department)
                .build();
        if (doctor.getEmail() == null || doctor.getLicenseNumber() == null) {
            throw new BaseException(ErrorCode.DOCTOR_DATA_INCOMPLETE, "Critical Doctor data is missing before save.");
        }
        Doctor saveDoctor = doctorRepository.save(doctor);

        return hospitalMapper.mapDoctorResponse(saveDoctor);
    }

    @Override
    @Transactional
    public PasswordResetResponse resetPassword(Long id, PasswordResetRequest request) {
        if (id == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Doctor ID cannot be null");
        }
        if (request == null || request.getNewPassword() == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Password reset request is invalid");
        }

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.DOCTOR_NOT_FOUND, "Doctor not found with ID: " + id));
        doctor.setPassword(passwordEncoder.encode(request.getNewPassword()));
        doctorRepository.save(doctor);

        return PasswordResetResponse.builder()
                .id(doctor.getId())
                .message("Doctor password reset successfully")
                .build();
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        if (id == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Doctor ID cannot be null");
        }
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.DOCTOR_NOT_FOUND, "Doctor not found with ID: " + id));
        if (doctor == null) {
            throw new BaseException(ErrorCode.OPERATION_FAILED, "Unexpected null entity returned from repository");
        }
        return hospitalMapper.mapDoctorResponse(doctor);
    }

    @Override
    public List<DoctorSummaryModel> getAllDoctorsForPatients() {
        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getDepartment() != null && doctor.getDepartment().isActive())
                .map(doctor -> {
                    DoctorSummaryModel summary = hospitalMapper.mapDoctorSummary(doctor);
                    // Set real availability based on appointment slots today
                    summary.setAvailableToday(hasSlotsAvailableToday(doctor.getId()));
                    return summary;
                })
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
