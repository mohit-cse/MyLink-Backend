package com.mohit.authnz_service.controllers;

import com.mohit.authnz_service.dto.authn.JwtTokenResponse;
import com.mohit.authnz_service.dto.authn.LoginRequest;
import com.mohit.authnz_service.dto.authn.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authn")
@Tag(name = "Authentication Management", description = "APIs for authentication")
public class AuthnController {

    @PostMapping("/login")
    @Operation(summary = "Client login", description = "API to login using credentials")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        // Logic to handle login
        // For now, just return a dummy response
        JwtTokenResponse jwtTokenResponse = JwtTokenResponse.builder()
                .token("dummyToken")
                .issuedAt(System.currentTimeMillis())
                .expiresAt(System.currentTimeMillis() + 3600000) // 1 hour later
                .sessionExpiresAt(System.currentTimeMillis() + 7200000) // 2 hours later
                .build();
        LoginResponse response = LoginResponse.builder()
                .userId("12365")
                .success(true)
                .jwtTokenResponse(jwtTokenResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Client logout", description = "API to logout a client")
    public ResponseEntity<String> logout(@RequestParam String userId) {
        // Logic to handle logout
        // For now, just return a success message
        return ResponseEntity.ok("User " + userId + " logged out successfully.");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "API to refresh the JWT token")
    public ResponseEntity<JwtTokenResponse> refresh(@RequestParam String token) {
        // Logic to refresh the token
        // For now, just return a dummy response
        JwtTokenResponse response = JwtTokenResponse.builder()
                .token("newDummyToken")
                .issuedAt(System.currentTimeMillis())
                .expiresAt(System.currentTimeMillis() + 3600000) // 1 hour later
                .build();
        return ResponseEntity.ok(response);
    }

}
