package com.mohit.modals.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Role {
    private String roleName;
}
