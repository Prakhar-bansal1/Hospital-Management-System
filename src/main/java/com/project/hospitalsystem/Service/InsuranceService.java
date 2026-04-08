package com.project.hospitalsystem.Service;

import com.project.hospitalsystem.Entity.Insurance;
import com.project.hospitalsystem.Model.InsuranceRequest;

public interface InsuranceService {
    
    Insurance manageInsurance(InsuranceRequest request);

    Insurance getInsuranceByPolicyNumber(String policyNumber);
}
