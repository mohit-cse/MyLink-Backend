package com.mohit.models.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class Role {
    private String roleName;
    private List<String> permissions;
}
