package com.project.hospitalsystem.Model;

import java.time.LocalDate;

import com.project.hospitalsystem.EnumType.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    
    private Long appointmentId;
    private LocalDate appointmentDate;
    private AppointmentStatus status;

    private String patientName;
    private String patientId;

    private String doctorId;
    private String doctorName;
    private String specialization;

    private String departmentName;

    private String bookingTimestamp;
    
}
