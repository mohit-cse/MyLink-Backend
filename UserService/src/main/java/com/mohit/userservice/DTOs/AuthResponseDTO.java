package com.mohit.userservice.DTOs;

public record AuthResponseDTO(boolean isAuthenticated, String userId) {
}
