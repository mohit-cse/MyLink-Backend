package com.mohit.userservice.Utils.Facade;

import com.mohit.userservice.Services.UserAccountService;
import org.springframework.stereotype.Component;

@Component
public class UserAccountFacade {
    private final UserAccountService userAccountService;
    UserAccountFacade(UserAccountService userAccountService){
        this.userAccountService = userAccountService;
    }


    public boolean isUserIdValid(String userId) {
        return userAccountService.isUserIdValid(userId);
    }
}
