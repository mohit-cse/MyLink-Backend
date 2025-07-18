package com.mohit.models.authentication;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class AuthUser {
    private String userId;
    private String name;
    private List<Role> roles;
    @Setter
    private boolean isAuthenticated;
}
