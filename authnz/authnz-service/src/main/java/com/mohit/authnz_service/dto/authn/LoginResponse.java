package com.mohit.authnz_service.dto.authn;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String userId;
    private Boolean success;
    private JwtTokenResponse jwtTokenResponse;
}
