package com.project.hospitalsystem.Repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT COUNT(a) FROM Appointment a " +
       "WHERE a.doctor.id = :doctorId " +
       "AND a.appointmentDate = :date " +
       "AND a.appointmentTime = :time " +
       "AND a.status != com.project.hospitalsystem.EnumType.AppointmentStatus.CANCELLED")
long countActiveAppointments(@Param("doctorId") Long doctorId, 
                             @Param("date") LocalDate date, 
                             @Param("time") LocalTime time);

}

