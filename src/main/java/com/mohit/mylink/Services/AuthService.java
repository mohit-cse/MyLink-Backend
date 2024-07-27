package com.mohit.mylink.Services;

import com.mohit.mylink.DTOs.SignupRequest;
import com.mohit.mylink.DTOs.SignupResponse;
import com.mohit.mylink.Data.UserProfile;
import com.mohit.mylink.Data.UserProfileRepository;
import com.mohit.mylink.Utils.Mappers.UserProfileMapper;
import com.mohit.mylink.Utils.UserIDGenerator;
import com.mohit.mylink.Utils.Validators.UserSignupValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserProfileRepository profileRepository;
    private final UserProfileMapper mapper;
    private static final int ID_LENGTH = 6;
    AuthService(UserProfileRepository profileRepository, UserProfileMapper mapper){
        this.profileRepository = profileRepository;
        this.mapper = mapper;
    }

    public String createRandomUserId() {
        String userId;
        do {
            userId = UserIDGenerator.generateId(ID_LENGTH);
        } while (profileRepository.existsById(userId));
        return userId;
    }

    public String encodePassword(String plainPassword){
        return new BCryptPasswordEncoder().encode(plainPassword);
    }

    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest) {
        UserProfile profile = mapper.mapToProfile(signupRequest);
        if(!UserSignupValidator.validate(profile)) return new ResponseEntity<>(new SignupResponse(false, null),HttpStatus.BAD_REQUEST);
        String userId = createRandomUserId();
        profile.setUserId(userId);
        profile.setPassword(encodePassword(profile.getPassword()));
        profileRepository.save(profile);
        return new ResponseEntity<>(new SignupResponse(true, userId),HttpStatus.CREATED);
    }
}
