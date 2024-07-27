package com.mohit.mylink.Controllers;

import com.mohit.mylink.DTOs.LoginRequest;
import com.mohit.mylink.DTOs.LoginResponse;
import com.mohit.mylink.DTOs.SignupRequest;
import com.mohit.mylink.DTOs.SignupResponse;
import com.mohit.mylink.Services.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class UserAccountController {
    private final UserAccountService service;
    UserAccountController(UserAccountService service){
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        return service.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return service.login(loginRequest);
    }
}
