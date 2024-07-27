package com.mohit.mylink.DTOs;

import lombok.Data;

@Data
public class SignupRequest {
    String name;
    String email;
    String countryCode;
    long phone;
    String password;
}
