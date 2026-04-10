package com.project.hospitalsystem.Mapper;

import org.springframework.stereotype.Component;

import com.project.hospitalsystem.Entity.Appointment;
import com.project.hospitalsystem.Model.AppointmentResponse;

@Component
public class AppointmentMapper {
    public AppointmentResponse mapAppointmentResponse(Appointment appointment) {
        if (appointment == null)
            return null;
        return AppointmentResponse.builder()
                .appointmentId(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .patientName(appointment.getPatient().getName())
                .patientId(appointment.getPatient().getId().toString())
                .doctorId(appointment.getDoctor().getId().toString())
                .doctorName(appointment.getDoctor().getName())
                .specialization(appointment.getDoctor().getSpecialization())
                .departmentName(appointment.getDoctor().getDepartment().getDepartmentName())
                .bookingTimestamp(appointment.getCreatedAt().toString())
                .build();
    }
}
