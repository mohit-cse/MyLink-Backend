package com.mohit.mylink.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;


public record SignupResponse(boolean isProfileCreated, String userId) { }
