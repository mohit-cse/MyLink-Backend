package com.mohit.userservice.DTOs;

import java.util.UUID;

public record AuthRequestDTO(String userId, UUID token) {
}
