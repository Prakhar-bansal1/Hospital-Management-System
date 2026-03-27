package com.project.hospitalsystem.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.Insurance;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    
}
