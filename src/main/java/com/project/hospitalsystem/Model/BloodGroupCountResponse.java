package com.project.hospitalsystem.Model;

import com.project.hospitalsystem.BloodGenderType.BloodGroupType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class BloodGroupCountResponse {

    private BloodGroupType bloodGroupType;
    private long count;

}
