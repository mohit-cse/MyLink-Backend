package com.mohit.userservice.Services;

import com.mohit.userservice.DTOs.*;
import com.mohit.userservice.Data.UserProfile;
import com.mohit.userservice.Data.UserProfileRepository;
import com.mohit.userservice.Utils.Exceptions.UserAlreadyExistsException;
import com.mohit.userservice.Utils.Generator.UserIDGenerator;
import com.mohit.userservice.Utils.Mappers.SignupRequestToUserProfileMapper;
import com.mohit.userservice.Utils.Validators.UserProfileValidator;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserProfileRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final int ID_LENGTH = 6;

    public AuthService(
            UserProfileRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean checkForUserExistence(UserProfile profile){
        return userRepository.existsByPhone(profile.getPhone()) || userRepository.existsByEmail(profile.getEmail());
    }

    @Transactional
    private String createRandomUserId() {
        String userId;
        do {
            userId = UserIDGenerator.generateId(ID_LENGTH);
        } while (userRepository.existsById(userId));
        return userId;
    }
    @Transactional
    private void saveProfile(UserProfile profile) throws UserAlreadyExistsException {
        if(checkForUserExistence(profile)) throw new UserAlreadyExistsException("User with phone: "
                + profile.getPhone() + " or email: "
                + profile.getEmail() + " already exists");
        userRepository.save(profile);
    }

    public ResponseEntity<SignupResponseDTO> signup(SignupRequestDTO signupRequest) {
        UserProfile profile = SignupRequestToUserProfileMapper.mapToProfile(signupRequest);
        if(!UserProfileValidator.validateSignup(profile)) return new ResponseEntity<>(new SignupResponseDTO(false, null),HttpStatus.BAD_REQUEST);
        profile.setPassword(passwordEncoder.encode(signupRequest.password()));
        String userId = createRandomUserId();
        profile.setUserId(userId);
        try{
            saveProfile(profile);
        }
        catch (UserAlreadyExistsException e){
            e.printStackTrace();
            return new ResponseEntity<>(new SignupResponseDTO(false, null),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new SignupResponseDTO(true, userId),HttpStatus.CREATED);
    }

    public UserProfile authenticateLogin(LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        return userRepository.findByEmail(loginRequest.email()).orElseThrow();
    }

}
