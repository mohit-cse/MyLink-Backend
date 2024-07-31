package com.mohit.userservice.Facade;

import com.mohit.userservice.Data.UserProfile;
import com.mohit.userservice.Data.UserProfileRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserProfileFacade {
    private final UserProfileRepository userRepository;

    public UserProfileFacade(
            UserProfileRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public UserProfile getProfileByEmail(String email){
        return userRepository.findByEmail(email).get();
    }
}
