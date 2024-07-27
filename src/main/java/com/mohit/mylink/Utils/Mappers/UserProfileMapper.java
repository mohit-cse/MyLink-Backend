package com.mohit.mylink.Utils.Mappers;

import com.mohit.mylink.DTOs.SignupRequest;
import com.mohit.mylink.Data.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {
    public UserProfile mapToProfile(SignupRequest request){
        UserProfile profile = new UserProfile();
        profile.setCountryCode(request.countryCode());
        profile.setName(request.name());
        profile.setEmail(request.email());
        profile.setPassword(request.password());
        profile.setPhone(request.phone());
        return profile;
    }
}
