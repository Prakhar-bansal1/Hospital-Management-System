package com.project.hospitalsystem.Service.Implementation;

import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Entity.Insurance;
import com.project.hospitalsystem.Exception.BaseException;
import com.project.hospitalsystem.Exception.ErrorCode;
import com.project.hospitalsystem.Model.InsuranceRequest;
import com.project.hospitalsystem.Repo.InsuranceRepository;
import com.project.hospitalsystem.Service.InsuranceService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InsuranceServiceImpl implements InsuranceService {
    private final InsuranceRepository insuranceRepository;

    @Override
    @Transactional
    public Insurance manageInsurance(InsuranceRequest request) {
        if (request == null || request.getPolicyNumber() == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Insurance Policy number is required.");
        }
        return insuranceRepository.findByPolicyNumber(request.getPolicyNumber())
                .orElseGet(() -> {
                    Insurance createInsurance = Insurance.builder()
                            .policyNumber(request.getPolicyNumber())
                            .insuranceProvider(request.getInsuranceProvider())
                            .expiryDate(request.getExpiryDate())
                            .build();

                    if (createInsurance == null) {
                        throw new BaseException(ErrorCode.INSURANCE_CREATE_FAILED, "Failed to create new insurance.");
                    }
                    return insuranceRepository.save(createInsurance);

                });

    }

    @Override
    public Insurance getInsuranceByPolicyNumber(String policyNumber) {
        return insuranceRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new BaseException(ErrorCode.INSURANCE_NOT_FOUND, "Insurance not found with policy number: " + policyNumber));
    }

}
