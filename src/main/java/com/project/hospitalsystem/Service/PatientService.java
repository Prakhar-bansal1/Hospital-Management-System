package com.project.hospitalsystem.Service;

import com.project.hospitalsystem.Model.PatientRequest;

import java.lang.String;

public interface PatientService {
    String registerPatient(PatientRequest request);
    void deactivatePatient(Long id);
}
