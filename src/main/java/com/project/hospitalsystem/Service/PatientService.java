package com.project.hospitalsystem.Service;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;

public interface PatientService {
    PatientResponse registerPatient(PatientRequest request);
    void deactivatePatient(Long id);
    PatientResponse getPatientById(Long id);
     PatientResponse updatePatient(Long id, PatientUpdateRequest request);
Slice<PatientResponse> getAllPatients(Pageable pageable);
    }
