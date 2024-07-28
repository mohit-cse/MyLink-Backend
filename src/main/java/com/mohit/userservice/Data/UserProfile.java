package com.mohit.userservice.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserProfile {
    @Id
    String userId;
    String name;
    String email;
    String countryCode;
    long phone;
    String password;
}
