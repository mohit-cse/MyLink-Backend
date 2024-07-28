package com.mohit.userservice.Utils.Mappers;

import com.mohit.userservice.DTOs.UserProfileDTO;
import com.mohit.userservice.Data.UserProfile;

public class UserProfileToLoginResponseMapper {
    public static UserProfileDTO mapToProfileDTO(UserProfile profile){
        return new UserProfileDTO(profile.getUserId(), profile.getName(), profile.getEmail(), profile.getCountryCode(), profile.getPhone());
    }
}
