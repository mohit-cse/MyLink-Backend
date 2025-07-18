package com.mohit.dto.role;

import com.mohit.models.authentication.Role;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private String roleName;
    private String description;
    private List<Role> roles;
    private List<String> permissions;
}
