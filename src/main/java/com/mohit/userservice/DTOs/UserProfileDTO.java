package com.mohit.userservice.DTOs;

public record UserProfileDTO(String userId, String name, String email, String countryCode, long phone) { }
