package com.project.hospitalsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceptionResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
}
