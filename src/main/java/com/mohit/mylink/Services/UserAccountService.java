package com.mohit.mylink.Services;

import com.mohit.mylink.DTOs.LoginRequest;
import com.mohit.mylink.DTOs.LoginResponse;
import com.mohit.mylink.DTOs.SignupRequest;
import com.mohit.mylink.DTOs.SignupResponse;
import com.mohit.mylink.Data.UserProfile;
import com.mohit.mylink.Data.UserProfileRepository;
import com.mohit.mylink.Utils.Exceptions.NoSuchUserException;
import com.mohit.mylink.Utils.Exceptions.UserAlreadyExistsException;
import com.mohit.mylink.Utils.Mappers.UserProfileMapper;
import com.mohit.mylink.Utils.Generator.UserIDGenerator;
import com.mohit.mylink.Utils.Validators.UserProfileValidator;
import org.checkerframework.checker.units.qual.N;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserAccountService {
    private final UserProfileRepository profileRepository;
    private final UserProfileMapper mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;
    private static final int ID_LENGTH = 6;
    UserAccountService(UserProfileRepository profileRepository, UserProfileMapper mapper, AuthService authService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.profileRepository = profileRepository;
        this.mapper = mapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authService = authService;
    }

    private String createRandomUserId() {
        String userId;
        do {
            userId = UserIDGenerator.generateId(ID_LENGTH);
        } while (profileRepository.existsById(userId));
        return userId;
    }

    private String encodePassword(String plainPassword){
        return bCryptPasswordEncoder.encode(plainPassword);
    }

    private boolean checkForUserExistence(UserProfile profile){
        return profileRepository.existsByPhone(profile.getPhone()) || profileRepository.existsByEmail(profile.getEmail());
    }

    private void saveProfile(UserProfile profile) throws UserAlreadyExistsException {
        if(checkForUserExistence(profile)) throw new UserAlreadyExistsException("User with phone: "
                + profile.getPhone() + " or email: "
                + profile.getEmail() + " already exists");
        profileRepository.save(profile);
    }

    private UserProfile findProfile(long phone) throws NoSuchUserException{
        Optional<UserProfile> profileOptional = profileRepository.findByPhone(phone);
        if(profileOptional.isEmpty()) throw new NoSuchUserException("No user exists with phone: " + phone);
        return profileOptional.get();
    }

    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest) {
        UserProfile profile = mapper.mapToProfile(signupRequest);
        if(!UserProfileValidator.validateSignup(profile)) return new ResponseEntity<>(new SignupResponse(false, null),HttpStatus.BAD_REQUEST);

        String userId = createRandomUserId();
        profile.setUserId(userId);
        profile.setPassword(encodePassword(profile.getPassword()));
        try{
            saveProfile(profile);
        }
        catch (UserAlreadyExistsException e){
            e.printStackTrace();
            return new ResponseEntity<>(new SignupResponse(false, null),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new SignupResponse(true, userId),HttpStatus.CREATED);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        if(!UserProfileValidator.validateLogin(loginRequest.countryCode(), loginRequest.phone()))
            return new ResponseEntity<>(new LoginResponse(false, null, null),HttpStatus.BAD_REQUEST);
        try{
            UserProfile profile = findProfile(loginRequest.phone());
            String passwordHash = encodePassword(loginRequest.password());
            if(!(loginRequest.countryCode().equals(profile.getCountryCode()) || passwordHash.equals(profile.getPassword())))
                return new ResponseEntity<>(new LoginResponse(false, null, null),HttpStatus.UNAUTHORIZED);
            Optional<UUID> tokenOptional = authService.isSessionActive(profile.getUserId());
            UUID token = tokenOptional.orElse(authService.generateToken(profile.getUserId()));
            return new ResponseEntity<>(new LoginResponse(true, profile.getUserId(), token), HttpStatus.OK);
        }
        catch (NoSuchUserException e){
            e.printStackTrace();
            return new ResponseEntity<>(new LoginResponse(false, null, null), HttpStatus.NOT_FOUND);
        }
    }
}
