package com.mohit.mylink.DTOs;

import java.util.UUID;

public record LoginResponse(boolean isAuthenticated, String userId, UUID authToken) { }
