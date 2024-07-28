package com.mohit.userservice.Utils.Mappers;

import com.mohit.userservice.DTOs.SignupRequestDTO;
import com.mohit.userservice.Data.UserProfile;

public class SignupRequestToUserProfileMapper {
    public static UserProfile mapToProfile(SignupRequestDTO request){
        UserProfile profile = new UserProfile();
        profile.setCountryCode(request.countryCode());
        profile.setName(request.name());
        profile.setEmail(request.email());
        profile.setPassword(request.password());
        profile.setPhone(request.phone());
        return profile;
    }
}
