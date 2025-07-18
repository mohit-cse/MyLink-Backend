package com.mohit.dto.profile;

import com.mohit.models.authentication.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisterClientResponse {
    private String clientId;
    private List<Role> roles;
}
