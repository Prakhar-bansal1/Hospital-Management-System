package com.project.hospitalsystem.Service.Implementation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.hospitalsystem.Entity.Role;
import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.Mapper.HospitalMapper;
import com.project.hospitalsystem.Service.ReceptionService;
import com.project.hospitalsystem.Model.AppointmentRequest;
import com.project.hospitalsystem.Model.AppointmentResponse;
import com.project.hospitalsystem.Model.PatientRequest;
import com.project.hospitalsystem.Model.PatientResponse;
import com.project.hospitalsystem.Model.PatientUpdateRequest;
import com.project.hospitalsystem.Model.PasswordResetRequest;
import com.project.hospitalsystem.Model.PasswordResetResponse;
import com.project.hospitalsystem.Model.ReceptionRequest;
import com.project.hospitalsystem.Model.ReceptionResponse;
import com.project.hospitalsystem.Repo.PatientRepository;
import com.project.hospitalsystem.Repo.UserRepository;
import com.project.hospitalsystem.Service.AppointmentService;
import com.project.hospitalsystem.Service.PatientService;

import java.util.Optional;
import java.util.Set;
import java.util.Objects;

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
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public PatientResponse addPatient(PatientRequest request) {
		return patientService.registerPatient(request);
	}

	public PatientResponse findPatientById(Long id) {
		return patientService.getPatientById(id);
	}

	public PatientResponse findPatientByPhone(String phone) {
		if (phone == null) throw new IllegalArgumentException("Phone number required");
		return patientRepository.findByPhoneNumber(phone)
				.map(hospitalMapper::mapPatientResponse)
				.orElseThrow(() -> new RuntimeException("Patient not found with phone: " + phone));
	}

	public Slice<PatientResponse> findPatientsByName(String name, Pageable pageable) {
		if (name == null) throw new IllegalArgumentException("Name is required");
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
			throw new IllegalArgumentException("Receptionist request cannot be null");
		}
		if (request.getPhoneNumber() == null || request.getEmail() == null || request.getPassword() == null) {
			throw new IllegalArgumentException("Receptionist name, email, phone number, and password are required");
		}
		Optional<User> existingByPhone = userRepository.findByPhoneNumber(request.getPhoneNumber());
		if (existingByPhone.isPresent()) {
			throw new IllegalStateException("User with this phone number already exists");
		}
		User receptionist = User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.phoneNumber(request.getPhoneNumber())
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(Set.of(Role.RECEPTION))
				.isActive(true)
				.build();
		User saved = userRepository.save(Objects.requireNonNull(receptionist));
		return ReceptionResponse.builder()
				.id(saved.getId())
				.name(saved.getName())
				.email(saved.getEmail())
				.phoneNumber(saved.getPhoneNumber())
				.role(Role.RECEPTION.name())
				.build();
	}

	@Override
	@Transactional
	public void deactivateReceptionist(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Receptionist ID cannot be null");
		}
		User receptionist = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Receptionist not found with ID: " + id));
		receptionist.setActive(false);
		userRepository.save(receptionist);
	}

	@Override
	@Transactional
	public PasswordResetResponse resetPassword(Long id, PasswordResetRequest request) {
		if (id == null) {
			throw new IllegalArgumentException("Receptionist ID cannot be null");
		}
		if (request == null || request.getNewPassword() == null) {
			throw new IllegalArgumentException("Password reset request is invalid");
		}

		User receptionist = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Receptionist not found with ID: " + id));
		receptionist.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(receptionist);

		return PasswordResetResponse.builder()
				.id(receptionist.getId())
				.message("Receptionist password reset successfully")
				.build();
	}

}
