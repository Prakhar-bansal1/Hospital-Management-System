package com.project.hospitalsystem.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.hospitalsystem.Entity.User;
import com.project.hospitalsystem.Repo.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email){
        User user=userRepository.findByEmail(email)
        .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        return UserPrincipal.create(user);
    }
}
