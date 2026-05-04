package com.project.hospitalsystem.Exception;

import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Handles Business Logic Errors (e.g., User Not Found)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseModel> handleBaseException(BaseException ex) {
        String traceId = UUID.randomUUID().toString();
        
        // Log the technical details (e.g., "Patient ID 101 missing")
        logger.error("Business Exception [TraceId: {}]: Code: {} - Technical Details: {}",
                traceId, ex.getErrorCode().getCode(), ex.getMessage());

        ErrorResponseModel response = new ErrorResponseModel(
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage(), // Safe user-friendly message
                System.currentTimeMillis(),
                traceId);
                
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 2. Handles Bean Validation Errors (e.g., @NotBlank, @Email)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseModel> handleValidation(MethodArgumentNotValidException ex) {
        String traceId = UUID.randomUUID().toString();

        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        logger.warn("Validation Failed [TraceID: {}]: {}", traceId, details);

        ErrorResponseModel response = new ErrorResponseModel(
                ErrorCode.VALIDATION_ERROR.getCode(), // Corrected name
                ErrorCode.VALIDATION_ERROR.getMessage(), 
                System.currentTimeMillis(),
                traceId);

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // 3. Catch-all for Security (Stops raw leaks like SQL errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseModel> handleGenericException(Exception ex) {
        String traceId = UUID.randomUUID().toString();

        logger.error("FATAL UNHANDLED ERROR [TraceID: {}]: ", traceId, ex);

        ErrorResponseModel response = new ErrorResponseModel(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                System.currentTimeMillis(),
                traceId);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}