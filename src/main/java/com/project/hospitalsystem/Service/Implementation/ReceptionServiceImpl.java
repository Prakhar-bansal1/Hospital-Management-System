package com.project.hospitalsystem.Service.Implementation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.hospitalsystem.Entity.Receptionist;
import com.project.hospitalsystem.Entity.Role;
import com.project.hospitalsystem.Exception.BaseException;
import com.project.hospitalsystem.Exception.ErrorCode;
import com.project.hospitalsystem.Mapper.HospitalMapper;
import com.project.hospitalsystem.Service.ReceptionService;
import com.project.hospitalsystem.Model.AppointmentRequest;
import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;
import com.project.hospitalsystem.Model.ReceptionRequest;
import com.project.hospitalsystem.Model.ReceptionResponse;
import com.project.hospitalsystem.Repo.PatientRepository;
import com.project.hospitalsystem.Repo.ReceptionistRepository;
import com.project.hospitalsystem.Service.AppointmentService;
import com.project.hospitalsystem.Service.PatientService;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReceptionServiceImpl implements ReceptionService {

	private final PatientService patientService;
	private final AppointmentService appointmentService;
	private final PatientRepository patientRepository;
	private final HospitalMapper hospitalMapper;
	private final ReceptionistRepository receptionistRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public PatientResponse addPatient(PatientRequest request) {
		return patientService.registerPatient(request);
	}

	public PatientResponse findPatientById(Long id) {
		return patientService.getPatientById(id);
	}

	public PatientResponse findPatientByPhone(String phone) {
		if (phone == null) throw new BaseException(ErrorCode.INVALID_INPUT, "Phone number is required.");
		return patientRepository.findByPhoneNumber(phone)
				.map(hospitalMapper::mapPatientResponse)
				.orElseThrow(() -> new BaseException(ErrorCode.PATIENT_NOT_FOUND, "Patient not found with phone: " + phone));
	}

	public Slice<PatientResponse> findPatientsByName(String name, Pageable pageable) {
		if (name == null) throw new BaseException(ErrorCode.INVALID_INPUT, "Patient name is required.");
		return patientRepository.findByNameStartingWithIgnoreCase(name, pageable)
				.map(hospitalMapper::mapPatientResponse);
	}

	@Transactional
	public PatientResponse updatePatient(Long id, PatientUpdateRequest request) {
		return patientService.updatePatient(id, request);
	}

	@Transactional
	public void deactivatePatient(Long id) {
		patientService.deactivatePatient(id);
	}

	@Transactional
	public AppointmentResponse bookAppointment(AppointmentRequest request) {
		return appointmentService.createAppointment(request);
	}

	public AppointmentResponse getAppointmentById(Long id) {
		return appointmentService.getAppointmentById(id);
	}

	public Slice<AppointmentResponse> getAppointmentsByPatient(Long patientId, Pageable pageable) {
		return appointmentService.getAppointmentsByPatient(patientId, pageable);
	}

	@Override
	@Transactional
	public ReceptionResponse registerReceptionist(ReceptionRequest request) {
		if (request == null) {
			throw new BaseException(ErrorCode.RECEPTIONIST_NULL_REQUEST, "Receptionist request cannot be null.");
		}
		if (request.getPhoneNumber() == null || request.getEmail() == null || request.getPassword() == null) {
			throw new BaseException(ErrorCode.INVALID_INPUT, "Receptionist name, email, phone number, and password are required.");
		}
		Optional<Receptionist> existingByPhone = receptionistRepository.findByPhoneNumber(request.getPhoneNumber());
		if (existingByPhone.isPresent()) {
			throw new BaseException(ErrorCode.RECEPTIONIST_PHONE_EXISTS, "Receptionist with this phone number already exists.");
		}
		if (receptionistRepository.existsByEmail(request.getEmail())) {
			throw new BaseException(ErrorCode.RECEPTIONIST_EMAIL_EXISTS, "Receptionist with this email already exists.");
		}
		Receptionist receptionist = Receptionist.builder()
				.name(request.getName())
				.email(request.getEmail())
				.phoneNumber(request.getPhoneNumber())
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(Set.of(Role.RECEPTION))
				.active(true)
				.build();
		Receptionist saved = receptionistRepository.save(Objects.requireNonNull(receptionist));
		return hospitalMapper.mapReceptionistResponse(saved, "Receptionist created successfully");
	}

	@Override
	@Transactional
	public void deactivateReceptionist(Long id) {
		if (id == null) {
			throw new BaseException(ErrorCode.INVALID_INPUT, "Receptionist ID cannot be null.");
		}
		Receptionist receptionist = receptionistRepository.findById(id)
				.orElseThrow(() -> new BaseException(ErrorCode.RECEPTIONIST_NOT_FOUND, "Receptionist not found with ID: " + id));
		receptionist.setActive(false);
		receptionistRepository.save(receptionist);
	}

 }
