package com.project.hospitalsystem.Service;

import java.util.List;

import com.project.hospitalsystem.Model.DoctorRequest;
import com.project.hospitalsystem.Model.DoctorResponse;

public interface DoctorService {
    DoctorResponse registerDoctor(DoctorRequest request);

    DoctorResponse getDoctorById(Long id);

    List<DoctorResponse> getAllDoctors();
}
