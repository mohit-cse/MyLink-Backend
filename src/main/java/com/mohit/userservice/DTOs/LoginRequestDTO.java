package com.mohit.userservice.DTOs;

public record LoginRequestDTO(String countryCode, long phone, String password) { }