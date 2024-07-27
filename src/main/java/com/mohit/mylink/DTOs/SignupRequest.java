package com.mohit.mylink.DTOs;

import lombok.Data;


public record SignupRequest(String name, String email, String countryCode, long phone, String password) { }
