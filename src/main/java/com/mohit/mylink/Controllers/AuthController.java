package com.mohit.mylink.Controllers;

import com.mohit.mylink.DTOs.SignupRequest;
import com.mohit.mylink.DTOs.SignupResponse;
import com.mohit.mylink.Services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    AuthController(AuthService service){
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        return service.signup(signupRequest);
    }

}
