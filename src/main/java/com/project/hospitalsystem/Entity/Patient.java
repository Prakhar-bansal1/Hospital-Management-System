package com.project.hospitalsystem.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.project.hospitalsystem.EnumType.BloodGroupType;
import com.project.hospitalsystem.EnumType.GenderType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// import lombok.ToString;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
// @ToString(exclude = "password")
@Table(name = "patients", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_phone", columnList = "phoneNumber"),
        @Index(name = "idx_bloodGroup", columnList = "bloodGroup"),
        @Index(name = "idx_email", columnList = "email")
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Setter
    @Column(nullable = false, length = 100) // Database-level constraint
    private String name;

    @Setter
    @Column(nullable = false, length = 10)
    // Not all family members may have a phone number, so we won't use "Unique
    // property"
    private String phoneNumber;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    // Not all patients may have an email, so we won't use @Notblank in email.
    @Setter
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dateofbirth;

    @Setter
    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @Setter
    @Column(nullable = false, length = 255)
    private String fullAddress;

    @Setter
    @Column(nullable = false, length = 100)
    private String city;

    @Setter
    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id", nullable = true)
    private Insurance insurance;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Role roles;
}
