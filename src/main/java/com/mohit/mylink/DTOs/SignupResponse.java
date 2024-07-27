package com.mohit.mylink.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResponse {
    boolean isProfileCreated;
    String userId;
}
