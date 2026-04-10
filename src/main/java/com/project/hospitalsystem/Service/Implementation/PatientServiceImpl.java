package com.project.hospitalsystem.Service.Implementation;

import com.project.hospitalsystem.Entity.Insurance;
import com.project.hospitalsystem.Entity.Patient;
import com.project.hospitalsystem.Mapper.HospitalMapper;
import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Repo.InsuranceRepository;
import com.project.hospitalsystem.Repo.PatientRepository;
import com.project.hospitalsystem.Service.InsuranceService;
import com.project.hospitalsystem.Service.PatientService;
import lombok.RequiredArgsConstructor;

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

    @Override
    @Transactional
    public PatientResponse registerPatient(PatientRequest request){
        if (request == null || request.getPhoneNumber() == null || request.getDateofbirth() == null) {
            throw new RuntimeException("Invalid data provided");
        }
        return patientRepository.findByPhoneNumber(request.getPhoneNumber())
                .map(existing -> {
                    if (existing.getDateofbirth() != null && existing.getDateofbirth().equals(request.getDateofbirth())) {
                        if (existing.isActive()) {
                            throw new RuntimeException("Error: This profile is already registered!");
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
            .password(request.getPassword())
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

            if (reactivated==null) {
                throw new IllegalStateException("Failed to reactivate patient");
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

                    // Insurance newInsurance = Insurance.builder()
                    //     .policyNumber(policyNumber)
                    //     .insuranceProvider(request.getInsurance().getInsuranceProvider())
                    //     .expiryDate(request.getInsurance().getExpiryDate())
                    //     .build();
                    //     if (newInsurance==null) {
                    //         throw new IllegalStateException("Failed to build insurance ");
                    //     }
                        return insuranceService.manageInsurance(request.getInsurance());
                    }
                );
        }
        Patient newPatient = Patient.builder()
            .name(request.getName())
            .dateofbirth(request.getDateofbirth())
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .password(request.getPassword())
            .bloodGroup(request.getBloodGroup())
            .fullAddress(request.getFullAddress())
            .city(request.getCity())
            .pincode(request.getPincode())
            .gender(request.getGender())
            .active(true)
            .insurance(insurance)
            .build();

            if (newPatient==null){
                throw new IllegalStateException("Failed to build Patient");
            }
        Patient saved = patientRepository.save(newPatient);
        return hospitalMapper.mapPatientResponse(saved);
    }

    @Override
        @Transactional
        public void deactivatePatient(Long id){
    if (id == null) {
        throw new IllegalArgumentException("Patient ID cannot be null");
    }

            Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient record not found "));
            Patient deactivatedPatient = patient.toBuilder()
            .active(false)
            .build();

            if (deactivatedPatient != null) {
            patientRepository.save(deactivatedPatient);
        }
    }
}