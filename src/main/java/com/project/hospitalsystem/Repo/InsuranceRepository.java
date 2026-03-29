package com.project.hospitalsystem.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.Insurance;


public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    Optional<Insurance> findByInsuranceProvider(String insuranceProvider);

    Optional<Insurance> findByPolicyNumber(String policyNumber);
}
