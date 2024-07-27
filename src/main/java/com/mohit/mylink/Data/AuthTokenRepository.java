package com.mohit.mylink.Data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {
    boolean existsByToken(UUID token);
}
