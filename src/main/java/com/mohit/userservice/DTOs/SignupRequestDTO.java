package com.mohit.userservice.DTOs;


public record SignupRequestDTO(String name, String email, String countryCode, long phone, String password) { }
