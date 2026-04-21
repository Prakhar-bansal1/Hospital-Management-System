package com.project.hospitalsystem.Security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.hospitalsystem.Entity.User;

import lombok.Getter;

@Getter
public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String email;

    @JsonIgnore
    private final String password;
    private final boolean isActive;
    private final Collection<? extends GrantedAuthority> authorities;

   public UserPrincipal(Long id, String email, String password, boolean isActive, Collection<? extends GrantedAuthority> authorities){
    this.id=id;
    this.email=email;
    this.password=password;
    this.isActive=isActive;
    this.authorities=authorities;
   }

    public static UserPrincipal create(User user) {
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword(), user.isActive(), authorities);

    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
