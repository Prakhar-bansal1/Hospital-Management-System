package com.project.hospitalsystem.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.hospitalsystem.Entity.Patient;
import com.project.hospitalsystem.Model.BloodGroupCountResponse;

public interface PatientRepository extends JpaRepository<Patient, Long>{
   Slice<Patient> findByNameStartingWithIgnoreCase(String name, Pageable pageable );

   Optional<Patient> findByPhoneNumber(String phoneNumber);

   Optional<Patient> findByInsuranceId(Long insuranceId);
   
   Optional<Patient> findByEmail(String email);
   
   // Custom query to count patients by blood group
   @Query("Select new com.project.hospitalsystem.Model.BloodGroupCountResponse(p.bloodGroup, count(p)) from Patient p group by p.bloodGroup")
   List<BloodGroupCountResponse> countPatientsByBloodGroup();
}
