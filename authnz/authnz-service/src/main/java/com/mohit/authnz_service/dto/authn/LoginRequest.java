package com.mohit.authnz_service.dto.authn;

import lombok.Data;

@Data
public class LoginRequest {
    private String clientId;
    private String clientSecret;
}
