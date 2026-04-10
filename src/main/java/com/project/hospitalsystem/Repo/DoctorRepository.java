package com.project.hospitalsystem.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // boolean check -> is data unique?
    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecializationIgnoreCase(String specialization);

    List<Doctor> findByDepartmentNameIgnoreCase(String departmentName);
}
