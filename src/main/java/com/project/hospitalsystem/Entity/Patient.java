package com.project.hospitalsystem.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.project.hospitalsystem.BloodType.BloodGroupType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "password")
@Table(name = "patients", indexes = {
    @Index(name = "idx_name", columnList = "name"),
    @Index(name = "idx_phone", columnList = "phonenumber"),
    @Index(name = "idx_bloodGroup", columnList = "bloodGroup")
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, length = 100) // Database-level constraint
    @Pattern(regexp = "^[a-zA-Z ]+$")     
    private String name;

    @Pattern(regexp = "^[0-9]{10}$")
    // Regex (Regular Expressions) is a tool to define patterns for string matching.
    @Column(nullable = false, length = 10)
    // Not all family members may have a phone number, so we won't use "Unique property" 
    private String phonenumber;

    @Column(nullable = false, length = 10)                   
    private String gender;

    // Not all patients may have an email, so we won't use @NotNull in email.
    @Email(message = "Invalid email format")  // Format validation
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dateofbirth;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @Column(nullable = false, length = 255)
    private String addressLine1;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 10)
    @Pattern(regexp = "^[1-9][0-9]{5}$") 
    private String pincode;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 100)
    private String password;
}
