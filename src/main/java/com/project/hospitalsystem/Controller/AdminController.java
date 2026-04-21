package com.project.hospitalsystem.Controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.DoctorRequest;
import com.project.hospitalsystem.Model.DoctorResponse;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Service.AppointmentService;
import com.project.hospitalsystem.Service.DoctorService;
import com.project.hospitalsystem.Service.PatientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sys/secureline/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping("/doctors")
    public ResponseEntity<DoctorResponse> registerDoctor(@RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.registerDoctor(request));
    }

    @GetMapping("/doctors/alldoctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/doctors/{id}.appointments")
    public ResponseEntity<Slice<AppointmentResponse>> getDoctorSchedule(@PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(id, pageable));
    }

    @GetMapping("/patients")
    public ResponseEntity<Slice<PatientResponse>> viewAllPatients(@PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }
}
