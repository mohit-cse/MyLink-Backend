package com.mohit.userservice.Controllers;

import com.mohit.userservice.DTOs.AuthRequestDTO;
import com.mohit.userservice.DTOs.AuthResponseDTO;
import com.mohit.userservice.Services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/authenticateToken")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO authRequestDTO){
        return authService.authenticate(authRequestDTO);
    }
}
