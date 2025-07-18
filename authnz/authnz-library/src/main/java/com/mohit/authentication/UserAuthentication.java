package com.mohit.authentication;

import com.mohit.models.authentication.AuthUser;
import com.mohit.models.authentication.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserAuthentication implements Authentication {
    private final AuthUser authUser;

    public UserAuthentication(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.getRoles().stream()
                .map(Role::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authUser;
    }

    @Override
    public boolean isAuthenticated() {
        return authUser.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authUser.setAuthenticated(isAuthenticated);
    }

    @Override
    public String getName() {
        return authUser.getName();
    }

    @Override
    public boolean implies(Subject subject) {
        return Authentication.super.implies(subject);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "UserAuthentication{" +
                "authUser=" + authUser +
                '}';
    }
}
