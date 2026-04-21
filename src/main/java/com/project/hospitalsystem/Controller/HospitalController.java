package com.project.hospitalsystem.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalsystem.Model.DoctorSummaryModel;
import com.project.hospitalsystem.Service.DoctorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
public class HospitalController {
    public final DoctorService doctorService;
    
    @GetMapping("/doctors/alldoctors")
    public ResponseEntity<List<DoctorSummaryModel>> getAllDoctorsforPatients() {
        return ResponseEntity.ok(doctorService.getAllDoctorsSummary());
    }
}