package com.mohit.userservice.Utils.Validators;

import com.mohit.userservice.Data.UserProfile;
import java.util.regex.Pattern;

public class UserProfileValidator {
    public static boolean validateSignup(UserProfile profile) {
        return validateName(profile.getName()) &&
                validatePhone(profile.getPhone()) &&
                validateEmail(profile.getEmail()) &&
                validateCountryCode(profile.getCountryCode()) &&
                validatePassword(profile.getPassword());
    }

    public static boolean validateLogin(String countryCode, long phone) {
        return validatePhone(phone) &&
                validateCountryCode(countryCode);
    }

    private static boolean validateName(String name){
        String nameRegex = "^[a-zA-Z\\s]+$";
        return name != null && name.length() >= 2 && Pattern.matches(nameRegex, name);
    }

    private static boolean validatePhone(long phone){
        return Long.toString(phone).length() == 10;
    }

    private static boolean validateCountryCode(String code){
        String regex = "^\\+[0-9]{1,4}$";
        return Pattern.matches(regex, code);
    }

    private static boolean validateEmail(String email){
        String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailRegex);
    }

    private static boolean validatePassword(String password){
        String passwordRegex = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=!\\-])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";
        return password.matches(passwordRegex);
    }
}
