package com.project.hospitalsystem.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseModel {
    String JWT;
    Long userId;
}
