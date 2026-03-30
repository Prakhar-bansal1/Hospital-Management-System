package com.project.hospitalsystem.Repo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.hospitalsystem.Entity.Appointment;

   @Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Slice<Appointment> findByDoctor_IdAndAppointmentDate(Long doctorId, LocalDate appointmentDate, Pageable pageable);

    Slice<Appointment> findByPatient_Id(Long patientId, Pageable pageable);

    Slice<Appointment> findByPatient_Insurance_PolicyNumber(String policyNumber, Pageable pageable);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor " +
           "WHERE a.doctor.id = :doctorId AND a.appointmentTime >= :start")
    Slice<Appointment> findUpcomingByDoctor(Long doctorId, LocalDateTime start, Pageable pageable);
}

