package com.project.hospitalsystem.Model;

import java.time.LocalDate;

import com.project.hospitalsystem.Entity.Doctor;
import com.project.hospitalsystem.Entity.Patient;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AppointmentRequest {

    @Column(nullable = false)
    private LocalDate appointmentDate;
    
    @Column(nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private Doctor doctor;

}