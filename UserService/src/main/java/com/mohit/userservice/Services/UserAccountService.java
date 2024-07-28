package com.mohit.userservice.Services;

import com.mohit.userservice.DTOs.LoginRequestDTO;
import com.mohit.userservice.DTOs.LoginResponseDTO;
import com.mohit.userservice.DTOs.SignupRequestDTO;
import com.mohit.userservice.DTOs.SignupResponseDTO;
import com.mohit.userservice.Data.UserProfile;
import com.mohit.userservice.Data.UserProfileRepository;
import com.mohit.userservice.Utils.Exceptions.NoSuchUserException;
import com.mohit.userservice.Utils.Exceptions.UserAlreadyExistsException;
import com.mohit.userservice.Utils.Facade.AuthFacade;
import com.mohit.userservice.Utils.Generator.UserIDGenerator;
import com.mohit.userservice.Utils.Mappers.SignupRequestToUserProfileMapper;
import com.mohit.userservice.Utils.Mappers.UserProfileToLoginResponseMapper;
import com.mohit.userservice.Utils.Validators.UserProfileValidator;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserAccountService {
    private final UserProfileRepository profileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthFacade authFacade;
    private static final int ID_LENGTH = 6;
    UserAccountService(UserProfileRepository profileRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthFacade authFacade){
        this.profileRepository = profileRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authFacade = authFacade;
    }

    @Transactional
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

    @Transactional
    private boolean checkForUserExistence(UserProfile profile){
        return profileRepository.existsByPhone(profile.getPhone()) || profileRepository.existsByEmail(profile.getEmail());
    }

    @Transactional
    private void saveProfile(UserProfile profile) throws UserAlreadyExistsException {
        if(checkForUserExistence(profile)) throw new UserAlreadyExistsException("User with phone: "
                + profile.getPhone() + " or email: "
                + profile.getEmail() + " already exists");
        profileRepository.save(profile);
    }

    @Transactional
    private UserProfile findProfileByPhone(long phone) throws NoSuchUserException{
        Optional<UserProfile> profileOptional = profileRepository.findByPhone(phone);
        if(profileOptional.isEmpty()) throw new NoSuchUserException("No user exists with phone: " + phone);
        return profileOptional.get();
    }

    private boolean isPasswordMatching(String plainPassword, String encodedHash){
        return bCryptPasswordEncoder.matches(plainPassword, encodedHash);
    }

    @Transactional
    public boolean isUserIdValid(String userId) throws NoSuchUserException{
        return profileRepository.existsById(userId);
    }

    public ResponseEntity<SignupResponseDTO> signup(SignupRequestDTO signupRequest) {
        UserProfile profile = SignupRequestToUserProfileMapper.mapToProfile(signupRequest);
        if(!UserProfileValidator.validateSignup(profile)) return new ResponseEntity<>(new SignupResponseDTO(false, null),HttpStatus.BAD_REQUEST);

        String userId = createRandomUserId();
        profile.setUserId(userId);
        profile.setPassword(encodePassword(profile.getPassword()));
        try{
            saveProfile(profile);
        }
        catch (UserAlreadyExistsException e){
            e.printStackTrace();
            return new ResponseEntity<>(new SignupResponseDTO(false, null),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new SignupResponseDTO(true, userId),HttpStatus.CREATED);
    }

    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequest) {
        if(!UserProfileValidator.validateLogin(loginRequest.countryCode(), loginRequest.phone()))
            return new ResponseEntity<>(new LoginResponseDTO(false, null, null),HttpStatus.BAD_REQUEST);
        try{
            UserProfile profile = findProfileByPhone(loginRequest.phone());
            if(!(loginRequest.countryCode().equals(profile.getCountryCode()) && profile.getPhone() == loginRequest.phone() && isPasswordMatching(loginRequest.password(), profile.getPassword())))
                return new ResponseEntity<>(new LoginResponseDTO(false, null, null),HttpStatus.UNAUTHORIZED);
            UUID token = authFacade.generateToken(profile.getUserId());
            return new ResponseEntity<>(new LoginResponseDTO(true, UserProfileToLoginResponseMapper.mapToProfileDTO(profile), token), HttpStatus.OK);
        }
        catch (NoSuchUserException e){
            e.printStackTrace();
            return new ResponseEntity<>(new LoginResponseDTO(false, null, null), HttpStatus.NOT_FOUND);
        }
    }
}
