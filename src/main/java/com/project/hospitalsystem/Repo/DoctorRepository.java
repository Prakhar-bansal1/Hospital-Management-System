package com.project.hospitalsystem.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
}
