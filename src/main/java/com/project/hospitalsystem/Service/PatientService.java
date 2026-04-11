package com.project.hospitalsystem.Service;

import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;

public interface PatientService {
    PatientResponse registerPatient(PatientRequest request);
    void deactivatePatient(Long id);
    PatientResponse updatePatient(Long id, PatientUpdateRequest request);
}
