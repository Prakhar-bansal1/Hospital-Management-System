package com.project.hospitalsystem.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecializationIgnoreCase(String specialization);

    List<Doctor> findByDepartmentName(String departmentName);
}
