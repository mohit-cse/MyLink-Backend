package com.mohit.userservice.Controllers;

import com.mohit.userservice.DTOs.*;
import com.mohit.userservice.Data.UserProfile;
import com.mohit.userservice.Services.AuthService;
import com.mohit.userservice.Services.JwtService;
import com.mohit.userservice.Utils.Mappers.UserProfileToLoginResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://api.mohit", maxAge = 3600)
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    AuthController(AuthService authService, JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
    }
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try{
            UserProfile authenticatedUser = authService.authenticateLogin(loginRequest);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            return new ResponseEntity<>(new LoginResponseDTO(true, UserProfileToLoginResponseMapper.mapToProfileDTO(authenticatedUser), jwtToken, jwtService.getExpirationTime()), HttpStatus.OK);
        }
        catch (AuthenticationException e){
            e.printStackTrace();
            return new ResponseEntity<>(new LoginResponseDTO(false, null, null, -1), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/authenticateToken")
    public ResponseEntity<AuthResponseDTO> authenticateToken(@RequestBody AuthRequestDTO authRequestDTO) {
        return jwtService.authenticateToken(authRequestDTO);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<Boolean> deleteAccount(@RequestBody LoginRequestDTO loginRequest) {
        return authService.deleteAccount(loginRequest);
    }
}
