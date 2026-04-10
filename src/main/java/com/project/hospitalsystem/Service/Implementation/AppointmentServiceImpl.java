package com.project.hospitalsystem.Service.Implementation;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Entity.Appointment;
import com.project.hospitalsystem.Entity.Doctor;
import com.project.hospitalsystem.Entity.Patient;
import com.project.hospitalsystem.Mapper.AppointmentMapper;
import com.project.hospitalsystem.Model.AppointmentRequest;
import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.AppointmentStatusUpdate;
import com.project.hospitalsystem.Repo.AppointmentRepository;
import com.project.hospitalsystem.Repo.DoctorRepository;
import com.project.hospitalsystem.Repo.PatientRepository;
import com.project.hospitalsystem.Service.AppointmentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {

        LocalDate date = request.getAppointmentDate();
        LocalTime time = request.getAppointmentTime();

        // No booking at current time and past time
        if (date.isBefore(LocalDate.now()) ||
                (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
            throw new IllegalArgumentException("Cannot book an appointment in the past!");
        }

        // Hospital Hours & 30-Min Format (9-5) with 30 mins booking slots
        if (time.getMinute() % 30 != 0 || time.getHour() < 9 || time.getHour() >= 17) {
            throw new IllegalArgumentException(
                    "Invalid slot! Please select slots like 09:30, 10:00 etc. between 09:00 and 17:00.");
        }

        // 10-Patient Limit per slot
        long currentBookings = appointmentRepository.countActiveAppointments(request.getDoctorId(), date, time);
        if (currentBookings >= 10) {
            throw new IllegalStateException(
                    "This time slot ( " + time + " ) is fully booked Please choose another time.");
        }

        if (request.getPatientId() == null || request.getDoctorId() == null) {
            throw new IllegalArgumentException("Patient and Doctor ID's must be provided!");
        }

        Long patId = request.getPatientId();
        Long docId = request.getDoctorId();
        if (patId == null || docId == null) {
            throw new IllegalArgumentException("Patient and Doctor IDs must be provided!");
        }
        Patient patient = patientRepository.findById(patId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = Appointment.builder()
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .patient(patient)
                .doctor(doctor)
                .build();
        if (appointment == null) {
            throw new IllegalStateException("Failed to create appointment");
        }
        Appointment save = appointmentRepository.save(appointment);
        return appointmentMapper.mapAppointmentResponse(save);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID must be provided!");
        }
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return appointmentMapper.mapAppointmentResponse(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatusUpdate updateRequest) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID must be provided!");
        }
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Appointment update = appointment.toBuilder()
                .status(updateRequest.getStatus())
                .build();
        if (update == null) {
            throw new IllegalStateException("Failed to create appointment");
        }

        return appointmentMapper.mapAppointmentResponse(appointmentRepository.save(update));
    }

    @Override
    public Slice<AppointmentResponse> getAppointmentsByPatient(Long patientId, Pageable pageable) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient Id required!");
        }
        return appointmentRepository.findByPatient_Id(patientId, pageable)
                .map(appointmentMapper::mapAppointmentResponse);
    }

    @Override
    public Slice<AppointmentResponse> getAppointmentsByDoctor(Long doctorId, Pageable pageable) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor Id required");
        }
        return appointmentRepository.findByDoctor_IdAndAppointmentDate(doctorId, LocalDate.now(), pageable)
                .map(appointmentMapper::mapAppointmentResponse);
    }
}
