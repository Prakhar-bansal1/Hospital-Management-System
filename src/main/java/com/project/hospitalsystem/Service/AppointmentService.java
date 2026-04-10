package com.project.hospitalsystem.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.project.hospitalsystem.Model.AppointmentRequest;
import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.AppointmentStatusUpdate;

public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentRequest request);

    AppointmentResponse getAppointmentById(Long id);

    AppointmentResponse updateStatus(Long id, AppointmentStatusUpdate updateRequest);

    Slice<AppointmentResponse> getAppointmentsByPatient(Long patientId, Pageable pageable);

    Slice<AppointmentResponse> getAppointmentsByDoctor(Long doctorId, Pageable pageable);
}
