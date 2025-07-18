package com.mohit.dto.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mohit.models.authentication.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisterClientRequest {
    private String clientId;
    @JsonIgnore
    private String clientSecret;
    private List<Role> roles;
}
