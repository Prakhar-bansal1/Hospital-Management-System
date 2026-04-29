package com.project.hospitalsystem.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalsystem.Model.AppointmentRequest;
import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;
import com.project.hospitalsystem.Model.PasswordResetRequest;
import com.project.hospitalsystem.Model.PasswordResetResponse;
import com.project.hospitalsystem.Service.AppointmentService;
import com.project.hospitalsystem.Service.PatientService;

import lombok.RequiredArgsConstructor;

import com.project.hospitalsystem.Security.UserPrincipal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/sys/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    // register patient
    @PostMapping("/register")
    public ResponseEntity<PatientResponse> registerPatient(@RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.registerPatient(request), HttpStatus.CREATED);
    }

    // get patient by id
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // update patient details by id
    @PutMapping("/update/{id}")
    @PreAuthorize("#id == UserPrincipal.id")
    public ResponseEntity<PatientResponse> updatepatient(@PathVariable Long id,
            @RequestBody PatientUpdateRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    // deactivate patient by id
    @DeleteMapping("/{id}/deactivate")
    @PreAuthorize("#id == UserPrincipal.id")
    public ResponseEntity<Void> deactivatepatient(@PathVariable Long id) {
        patientService.deactivatePatient(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password/reset")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PasswordResetResponse> resetPassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PasswordResetRequest request) {
        return ResponseEntity.ok(patientService.resetPassword(userPrincipal.getId(), request));
    }

    @GetMapping("/my-appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Slice<AppointmentResponse>> getMyHistory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientUser(userPrincipal.getId(), pageable));
    }

    @PutMapping("/{id}/appointment/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(@PathVariable Long id,
            @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }
}
