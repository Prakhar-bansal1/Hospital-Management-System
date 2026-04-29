package com.project.hospitalsystem.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.project.hospitalsystem.Model.AppointmentRequest;
import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;
import com.project.hospitalsystem.Model.PasswordResetRequest;
import com.project.hospitalsystem.Model.PasswordResetResponse;
import com.project.hospitalsystem.Model.ReceptionRequest;
import com.project.hospitalsystem.Model.ReceptionResponse;

public interface ReceptionService {

    PatientResponse addPatient(PatientRequest request);

    PatientResponse findPatientById(Long id);

    PatientResponse findPatientByPhone(String phone);

    Slice<PatientResponse> findPatientsByName(String name, Pageable pageable);

    PatientResponse updatePatient(Long id, PatientUpdateRequest request);

    void deactivatePatient(Long id);

    AppointmentResponse bookAppointment(AppointmentRequest request);

    AppointmentResponse getAppointmentById(Long id);

    Slice<AppointmentResponse> getAppointmentsByPatient(Long patientId, Pageable pageable);

    ReceptionResponse registerReceptionist(ReceptionRequest request);

    PasswordResetResponse resetPassword(Long id, PasswordResetRequest request);

    void deactivateReceptionist(Long id);

}
