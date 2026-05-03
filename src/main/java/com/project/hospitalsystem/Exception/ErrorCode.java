package com.project.hospitalsystem.Exception;

public enum ErrorCode {
    USER_NOT_FOUND("ERR_001", "User not found"),
    INVALID_CREDENTIALS("ERR_002", "Invalid credentials"),
    ACCESS_DENIED("ERR_003", "Access denied"),
    INTERNAL_SERVER_ERROR("ERR_004", "Internal server error");

    private final String code;
    private final String message;

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
