package com.mohit.dto.authn;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenResponse {
    private String userId;
    private String token;
    private long issuedAt;
    private long expiresAt;
    private long sessionExpiresAt;
    private String refreshToken;
    private long refreshTokenExpiresAt;
}
