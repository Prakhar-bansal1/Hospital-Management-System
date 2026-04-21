package com.project.hospitalsystem.Controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.AppointmentStatusUpdate;
import com.project.hospitalsystem.Security.UserPrincipal;
import com.project.hospitalsystem.Service.AppointmentService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/sys/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final AppointmentService appointmentService;

    @GetMapping("/my-schedule")
    public ResponseEntity<Slice<AppointmentResponse>> getMySchedule(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 15) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorUser(userPrincipal.getId(), pageable));
    }

     @PutMapping("/appointment/status/update")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id,
            @RequestBody AppointmentStatusUpdate request,
        @AuthenticationPrincipal UserPrincipal doctorPrincipal){
        return ResponseEntity.ok(appointmentService.updateStatus(id, request, doctorPrincipal.getId() ));
    }
}
