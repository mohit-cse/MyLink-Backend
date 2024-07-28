package com.mohit.userservice.Controllers;

import com.mohit.userservice.DTOs.LoginRequestDTO;
import com.mohit.userservice.DTOs.LoginResponseDTO;
import com.mohit.userservice.DTOs.SignupRequestDTO;
import com.mohit.userservice.DTOs.SignupResponseDTO;
import com.mohit.userservice.Services.UserAccountService;
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
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequest) {
        return service.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return service.login(loginRequest);
    }
}
