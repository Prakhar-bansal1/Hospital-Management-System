package com.project.hospitalsystem.Service.Implementation;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.project.hospitalsystem.Entity.Appointment;
import com.project.hospitalsystem.Entity.Doctor;
import com.project.hospitalsystem.Entity.Patient;
import com.project.hospitalsystem.Exception.BaseException;
import com.project.hospitalsystem.Exception.ErrorCode;
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
            throw new BaseException(ErrorCode.APPOINTMENT_INVALID_TIME, "Cannot book an appointment in the past.");
        }

        // Hospital Hours & 30-Min Format (9-5) with 30 mins booking slots
        if (time.getMinute() % 30 != 0 || time.getHour() < 9 || time.getHour() >= 17) {
            throw new BaseException(ErrorCode.APPOINTMENT_INVALID_SLOT, "Invalid slot! Please select slots like 09:30, 10:00 etc. between 09:00 and 17:00.");
        }

        // 10-Patient Limit per slot
        long currentBookings = appointmentRepository.countActiveAppointments(request.getDoctorId(), date, time);
        if (currentBookings >= 10) {
            throw new BaseException(ErrorCode.APPOINTMENT_SLOT_FULL, "This time slot (" + time + ") is fully booked. Please choose another time.");
        }

        if (request.getPatientId() == null || request.getDoctorId() == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient and Doctor ID's must be provided.");
        }

        Long patId = request.getPatientId();
        Long docId = request.getDoctorId();
        if (patId == null || docId == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient and Doctor IDs must be provided.");
        }
        Patient patient = patientRepository.findById(patId)
                .orElseThrow(() -> new BaseException(ErrorCode.PATIENT_NOT_FOUND, "Patient not found."));

        Doctor doctor = doctorRepository.findById(docId)
                .orElseThrow(() -> new BaseException(ErrorCode.DOCTOR_NOT_FOUND, "Doctor not found."));

        Appointment appointment = Appointment.builder()
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .patient(patient)
                .doctor(doctor)
                .build();
        if (appointment == null) {
            throw new BaseException(ErrorCode.APPOINTMENT_CREATE_FAILED, "Failed to create appointment.");
        }
        Appointment save = appointmentRepository.save(appointment);
        return appointmentMapper.mapAppointmentResponse(save);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        if (id == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Appointment ID must be provided.");
        }
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.APPOINTMENT_NOT_FOUND, "Appointment not found."));
        return appointmentMapper.mapAppointmentResponse(appointment);
    }

@Override
@Transactional
public AppointmentResponse updateStatus(Long id, AppointmentStatusUpdate updateRequest, Long authenticatedDoctorId) {
    if (id == null) {
        throw new BaseException(ErrorCode.INVALID_INPUT, "Appointment ID must be provided.");
    }
    Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new BaseException(ErrorCode.APPOINTMENT_NOT_FOUND, "Appointment not found."));
    if (!appointment.getDoctor().getId().equals(authenticatedDoctorId)) {
        throw new BaseException(ErrorCode.UNAUTHORIZED_ACCESS, "Unauthorized access to this resource.");
    }
        Appointment update = appointment.toBuilder()
                .status(updateRequest.getStatus())
                .build();
        if (update == null) {
            throw new BaseException(ErrorCode.APPOINTMENT_CREATE_FAILED, "Failed to update appointment.");
        }

        return appointmentMapper.mapAppointmentResponse(appointmentRepository.save(update));
    }

    

    @Override
    public Slice<AppointmentResponse> getAppointmentsByPatient(Long patientId, Pageable pageable) {
        if (patientId == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Patient ID is required.");
        }
        return appointmentRepository.findByPatient_Id(patientId, pageable)
                .map(appointmentMapper::mapAppointmentResponse);
    }

    @Override
    public Slice<AppointmentResponse> getAppointmentsByDoctor(Long doctorId, Pageable pageable) {
        if (doctorId == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "Doctor ID is required.");
        }
        return appointmentRepository.findByDoctor_IdAndAppointmentDate(doctorId, LocalDate.now(), pageable)
                .map(appointmentMapper::mapAppointmentResponse);
    }

    @Override
    public Slice<AppointmentResponse> getAppointmentsByPatientUser(Long userId, Pageable pageable) {
        if (userId == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "User ID is required.");
        }
        Patient patient = patientRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.PATIENT_NOT_FOUND, "Patient profile not found for this user."));
        return appointmentRepository.findByPatient_Id(patient.getId(), pageable)
                .map(appointmentMapper::mapAppointmentResponse);
    }

    @Override
    public Slice<AppointmentResponse> getAppointmentsByDoctorUser(Long userId, Pageable pageable) {
        if (userId == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT, "User ID is required.");
        }
        Doctor doctor = doctorRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.DOCTOR_NOT_FOUND, "Doctor profile not found for this user."));

        // 2. Fetch schedule (usually for current date/future)
        return appointmentRepository
                .findByDoctor_IdAndAppointmentDate(doctor.getId(), java.time.LocalDate.now(), pageable)
                .map(appointmentMapper::mapAppointmentResponse);
    }
}
