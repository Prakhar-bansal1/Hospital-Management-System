package com.project.hospitalsystem.Service;

import java.util.List;

import com.project.hospitalsystem.Model.DoctorRequest;
import com.project.hospitalsystem.Model.DoctorResponse;
import com.project.hospitalsystem.Model.DoctorSummaryModel;
import com.project.hospitalsystem.Model.PasswordResetRequest;
import com.project.hospitalsystem.Model.PasswordResetResponse;

public interface DoctorService {
    DoctorResponse registerDoctor(DoctorRequest request);

    PasswordResetResponse resetPassword(Long id, PasswordResetRequest request);

    DoctorResponse getDoctorById(Long id);

    List<DoctorResponse> getAllDoctors();

    List<DoctorSummaryModel> getAllDoctorsSummary();

    List<DoctorSummaryModel> getAllDoctorsForPatients();
}
