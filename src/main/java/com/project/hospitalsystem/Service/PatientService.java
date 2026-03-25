package com.project.hospitalsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.project.hospitalsystem.Entity.Patient;
import com.project.hospitalsystem.Repo.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Page<Patient> getPatientByNameOrPhoneNumber(String name, String phoneNumber, Pageable pageable) {
        return patientRepository.findByNameOrPhoneNumber(name, phoneNumber, pageable);
    }
}
