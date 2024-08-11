package com.mohit.docservice.DTOs;

public record AuthResponseDTO(boolean isAuthenticated, String userId) { }