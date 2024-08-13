package com.mohit.userservice.Data;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    boolean existsByPhone(long phone);
    boolean existsByEmail(String email);

    Optional<UserProfile> findByEmail(String email);

    Optional<UserProfile> findByPhone(long phone);
    void deleteByEmail(String email);
}
