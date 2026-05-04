package com.project.hospitalsystem.Exception;

public record ErrorResponseModel(
    String code,
    String message,
    long timestamp,
    String traceId
) {} 
 