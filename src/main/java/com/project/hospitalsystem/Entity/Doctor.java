package com.project.hospitalsystem.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.project.hospitalsystem.BloodGenderType.BloodGroupType;
import com.project.hospitalsystem.BloodGenderType.GenderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "doctors", indexes = {
    @Index(name = "idx_doctor_name", columnList = "name"),
    @Index(name = "idx_doctor_email", columnList = "email")
})
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable=false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(nullable = false,length = 100)
    private String specialization;

    @Column(nullable = false, unique = true, length = 10)
    private String licenseNumber;

    @Column(nullable = false,length = 10, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)  // owner side because many doctors can have one department
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}