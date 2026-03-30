package com.project.hospitalsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorSummaryModel {
    private Long id; 
    
    private String name; 
    
    private String specialization; 
    
    private String departmentName;
    
    private String qualification; 
    
    private double consultationFee; 
    
    private boolean isAvailableToday;
}
