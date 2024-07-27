package com.mohit.mylink.Utils.Mappers;

import com.mohit.mylink.DTOs.SignupRequest;
import com.mohit.mylink.Data.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {
    public UserProfile mapToProfile(SignupRequest request){
        UserProfile profile = new UserProfile();
        profile.setCountryCode(request.getCountryCode());
        profile.setName(request.getName());
        profile.setEmail(request.getEmail());
        profile.setPassword(request.getPassword());
        profile.setPhone(request.getPhone());
        return profile;
    }
}
