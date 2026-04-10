package com.project.hospitalsystem.Service;

import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;

public interface PatientService {
    PatientResponse registerPatient(PatientRequest request);
    void deactivatePatient(Long id);
}
