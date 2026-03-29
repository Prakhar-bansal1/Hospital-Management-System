package com.project.hospitalsystem.Model;

import com.project.hospitalsystem.EnumType.AppointmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentStatusUpdate {
    
    @NotNull
    private AppointmentStatus status;

    private String updateBy;
}
