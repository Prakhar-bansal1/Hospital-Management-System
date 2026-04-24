package com.project.hospitalsystem.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.EnumType.AuthProvidertype;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderIdAndProviderType(String providerId, AuthProvidertype providerType);
}
