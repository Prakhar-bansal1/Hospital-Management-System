package com.project.hospitalsystem.Exception;

public enum ErrorCode {
    // General Errors
    USER_NOT_FOUND("ERR_001", "The requested resource was not found."),
    VALIDATION_ERROR("ERR_002", "Invalid input provided."),
    UNAUTHORIZED("ERR_003", "Authentication is required."),
    INTERNAL_SERVER_ERROR("ERR_004", "An unexpected error occurred."),

    
    // Patient Errors
    PATIENT_NOT_FOUND("ERR_005", "Patient not found."),
    PATIENT_ALREADY_REGISTERED("ERR_006", "Patient is already registered."),
    PATIENT_REACTIVATION_FAILED("ERR_007", "Failed to reactivate patient."),
    INSURANCE_SAVE_FAILED("ERR_008", "Failed to save insurance details."),
    PATIENT_BUILD_FAILED("ERR_009", "Failed to create patient record."),

    // Doctor Errors
    DOCTOR_NOT_FOUND("ERR_010", "Doctor not found."),
    DOCTOR_LICENSE_EXISTS("ERR_011", "Doctor license number already exists."),
    DOCTOR_EMAIL_EXISTS("ERR_012", "Doctor email already exists."),
    DOCTOR_DATA_INCOMPLETE("ERR_013", "Critical doctor data is missing."),

    // Department Errors
    DEPARTMENT_NOT_FOUND("ERR_014", "Department not found."),

    // Appointment Errors
    APPOINTMENT_NOT_FOUND("ERR_015", "Appointment not found."),
    APPOINTMENT_INVALID_TIME("ERR_016", "Cannot book an appointment in the past."),
    APPOINTMENT_INVALID_SLOT("ERR_017",
            "Invalid appointment slot. Slots must be in 30-minute intervals between 09:00 and 17:00."),
    APPOINTMENT_SLOT_FULL("ERR_018", "This appointment slot is fully booked."),
    APPOINTMENT_CREATE_FAILED("ERR_019", "Failed to create appointment."),

    // Insurance Errors
    INSURANCE_NOT_FOUND("ERR_020", "Insurance not found."),
    INSURANCE_CREATE_FAILED("ERR_021", "Failed to create insurance record."),

    // Receptionist Errors
    RECEPTIONIST_NOT_FOUND("ERR_022", "Receptionist not found."),
    RECEPTIONIST_PHONE_EXISTS("ERR_023", "Receptionist with this phone number already exists."),
    RECEPTIONIST_EMAIL_EXISTS("ERR_024", "Receptionist with this email already exists."),
    RECEPTIONIST_NULL_REQUEST("ERR_025", "Receptionist request cannot be null."),

    // Authorization Errors
    UNAUTHORIZED_ACCESS("ERR_026", "Unauthorized access to this resource."),

    // Generic Errors
    INVALID_INPUT("ERR_027", "Invalid input provided."),
    OPERATION_FAILED("ERR_028", "Operation failed.");

    private final String code;
    private final String message;

    // const..
    // const..
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
