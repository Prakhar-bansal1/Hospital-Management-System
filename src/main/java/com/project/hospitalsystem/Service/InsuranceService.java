package com.project.hospitalsystem.Service;

import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Repo.InsuranceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private InsuranceRepository insuranceRepository;
}
