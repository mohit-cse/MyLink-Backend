package com.mohit.userservice.DTOs;

import java.util.UUID;

public record LoginResponseDTO(boolean isAuthenticated, UserProfileDTO profileDTO, String token, long expiry) { }
