package com.project.hospitalsystem.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) 
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
@Table(name = "insurance", indexes = {
    @Index(name = "idx_policy_no", columnList = "policy_number", unique = true)
})
public class Insurance {  
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String policyNumber;

    @Column(nullable = false, length = 100)
    private String insuranceProvider;

    @Column(nullable = false)
    private LocalDate expiryDate;
    public boolean isExpired() {
    return expiryDate != null && expiryDate.isBefore(LocalDate.now());
}

}
