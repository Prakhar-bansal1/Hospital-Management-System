package com.project.hospitalsystem.Controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalsystem.Model.AppointmentRequest;
import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.AppointmentStatusUpdate;
import com.project.hospitalsystem.Model.DoctorSummaryModel;
import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;
import com.project.hospitalsystem.Security.UserPrincipal;
import com.project.hospitalsystem.Service.AppointmentService;
import com.project.hospitalsystem.Service.DoctorService;
import com.project.hospitalsystem.Service.PatientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sys/internal/reception")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECEPTION')")
public class ReceptionController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @GetMapping("/doctor/{id}.appointments")
    public ResponseEntity<Slice<AppointmentResponse>> getDoctorSchedule(@PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(id, pageable));
    }

    @GetMapping("/doctors/alldoctors")
    public ResponseEntity<List<DoctorSummaryModel>> getAllDoctorsSummary() {
        return ResponseEntity.ok(doctorService.getAllDoctorsSummary());
    }

    @GetMapping("/patients")
    public ResponseEntity<Slice<PatientResponse>> viewAllPatients(@PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }

    @PostMapping("/patient/register")
    public ResponseEntity<PatientResponse> registerPatient(@RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.registerPatient(request), HttpStatus.CREATED);
    }

    @PutMapping("/patient/update/{id}")
    public ResponseEntity<PatientResponse> updatepatient(@PathVariable Long id,
            @RequestBody PatientUpdateRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @PutMapping("/patient/{id}/appointment/status/update")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id,
            @RequestBody AppointmentStatusUpdate request,
            @AuthenticationPrincipal UserPrincipal doctorPrincipal) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request, doctorPrincipal.getId()));
    }

    @PutMapping("/patient/{id}/appointment/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(@PathVariable Long id,
            @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }

    @DeleteMapping("/patient/{id}/deactivate")
    public ResponseEntity<Void> deactivatepatient(@PathVariable Long id) {
        patientService.deactivatePatient(id);
        return ResponseEntity.noContent().build();
    }
}
