package com.mohit.userservice.Data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {
    boolean existsByToken(UUID token);

    Optional<AuthToken> findByToken(UUID token);
}
