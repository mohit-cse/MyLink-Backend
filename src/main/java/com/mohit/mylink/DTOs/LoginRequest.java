package com.mohit.mylink.DTOs;

public record LoginRequest(String countryCode, long phone, String password) { }