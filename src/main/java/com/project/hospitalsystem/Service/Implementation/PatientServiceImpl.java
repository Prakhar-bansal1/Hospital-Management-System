package com.project.hospitalsystem.Service.Implementation;

import com.project.hospitalsystem.Entity.Insurance;
import com.project.hospitalsystem.Entity.Patient;
import com.project.hospitalsystem.Exception.BaseException;
import com.project.hospitalsystem.Exception.ErrorCode;
import com.project.hospitalsystem.Mapper.HospitalMapper;
import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;
import com.project.hospitalsystem.Model.PasswordResetRequest;
import com.project.hospitalsystem.Model.PasswordResetResponse;
import com.project.hospitalsystem.Repo.InsuranceRepository;
import com.project.hospitalsystem.Repo.PatientRepository;
import com.project.hospitalsystem.Service.InsuranceService;
import com.project.hospitalsystem.Service.PatientService;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final InsuranceRepository insuranceRepository;
    private final InsuranceService insuranceService;
    private final HospitalMapper hospitalMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PatientResponse registerPatient(PatientRequest request) {
        if (request == null || request.getPhoneNumber() == null || request.getDateofbirth() == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient request must contain phone number and date of birth");
        }
        return patientRepository.findByPhoneNumber(request.getPhoneNumber())
                .map(existing -> {
                    if (existing.getDateofbirth() != null && existing.getId() != null
                    && existing.getId().equals(existing.getId())
                            && existing.getDateofbirth().equals(request.getDateofbirth())) {
                        if (existing.isActive()) {
                            throw new BaseException(ErrorCode.PATIENT_ALREADY_REGISTERED, "Patient with this phone number is already registered");
                        }
                        return reactivateExistingPatient(existing, request);
                    } else {
                        return createNewPatient(request);
                    }
                })
                .orElseGet(() -> createNewPatient(request));
    }

    private PatientResponse reactivateExistingPatient(Patient existing, PatientRequest request) {
        Patient reactivated = existing.toBuilder()
                .active(true)
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .fullAddress(request.getFullAddress())
                .city(request.getCity())
                .pincode(request.getPincode())
                .name(request.getName())

                .gender(existing.getGender())
                .insurance(existing.getInsurance())
                .bloodGroup(existing.getBloodGroup())
                .dateofbirth(existing.getDateofbirth())
                .build();

        if (reactivated == null) {
            throw new BaseException(ErrorCode.PATIENT_REACTIVATION_FAILED, "Error occurred while building reactivated patient record");
        }
        patientRepository.save(reactivated);
        return hospitalMapper.mapPatientResponse(reactivated);
    }

    private PatientResponse createNewPatient(PatientRequest request) {
        Insurance insurance = null;
        if (request.getInsurance() != null && request.getInsurance().getPolicyNumber() != null) {
            String policyNumber = request.getInsurance().getPolicyNumber();
            insurance = insuranceRepository.findByPolicyNumber(policyNumber)
                    .orElseGet(() -> {

                        Insurance newInsurance = Insurance.builder()
                        .policyNumber(policyNumber)
                        .insuranceProvider(request.getInsurance().getInsuranceProvider())
                        .expiryDate(request.getInsurance().getExpiryDate())
                        .build();
                        if (newInsurance==null) {
                        throw new BaseException(ErrorCode.INSURANCE_SAVE_FAILED, "Failed to save insurance for patient");
                        }
                        return insuranceService.manageInsurance(request.getInsurance());
                    });
        }

        Patient newPatient = Patient.builder()
                .name(request.getName())
                .dateofbirth(request.getDateofbirth())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
                .bloodGroup(request.getBloodGroup())
                .fullAddress(request.getFullAddress())
                .city(request.getCity())
                .pincode(request.getPincode())
                .gender(request.getGender())
                .active(true)
                .insurance(insurance)
                .build();

        if (newPatient == null) {
            throw new BaseException(ErrorCode.PATIENT_BUILD_FAILED, "Error occurred while building patient record");
        }
        Patient saved = patientRepository.save(newPatient);
        return hospitalMapper.mapPatientResponse(saved);
    }

    @Override
    @Transactional
    public void deactivatePatient(Long id) {
        if (id == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient ID cannot be null");
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.PATIENT_NOT_FOUND, "Patient with ID " + id + " not found"));
        Patient deactivatedPatient = patient.toBuilder()
                .active(false)
                .build();

        if (deactivatedPatient != null) {
            patientRepository.save(deactivatedPatient);
        }
    }

    @Override
    @Transactional
    public PasswordResetResponse resetPassword(Long id, PasswordResetRequest request) {
        if (id == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient ID cannot be null");
        }
        if (request == null || request.getNewPassword() == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Password reset request with new password is required");
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.PATIENT_NOT_FOUND, "Patient with ID " + id + " not found"));

        if (request.getCurrentPassword() == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Current password is required for verification");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), patient.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Current password is incorrect");
        }

        Patient updatedPatient = patient.toBuilder()
                .password(passwordEncoder.encode(request.getNewPassword()))
                .build();
        if(updatedPatient == null) {
            throw new BaseException(ErrorCode.OPERATION_FAILED, "Failed to update patient password");
        }
        patientRepository.save(updatedPatient);
        return PasswordResetResponse.builder()
                .id(updatedPatient.getId())
                .message("Patient password reset successfully")
                .build();
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, PatientUpdateRequest request) {
        if (id == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient ID cannot be null");
        }
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.PATIENT_NOT_FOUND, "Patient with ID " + id + " not found"));
        hospitalMapper.updatePatientFromRequest(request, existingPatient);
        if (existingPatient == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient ID cannot be null");
        }
        Patient savedPatient = patientRepository.save(existingPatient);
        return hospitalMapper.mapPatientResponse(savedPatient);
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        if (id == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient ID cannot be null");
        }
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.PATIENT_NOT_FOUND, "Patient with ID " + id + " not found"));
        return hospitalMapper.mapPatientResponse(patient);
    }

    @Override
    public Slice<PatientResponse> getAllPatients(Pageable pageable) {
        if (pageable == null) {
        throw new BaseException(ErrorCode.INVALID_INPUT, "Pagination information (Pageable) cannot be null");
    }
       return patientRepository.findAll(pageable)
                .map(hospitalMapper::mapPatientResponse);
    }
}