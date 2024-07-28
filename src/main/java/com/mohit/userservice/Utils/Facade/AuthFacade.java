package com.mohit.userservice.Utils.Facade;

import com.mohit.userservice.Services.AuthService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthFacade {
    private final AuthService authService;
    AuthFacade(AuthService authService){
        this.authService = authService;
    }

    public UUID generateToken(String userId){
        return authService.generateToken(userId);
    }

}
