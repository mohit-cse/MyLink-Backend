package com.mohit.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/auth-test")
public class AuthTestController {
    @GetMapping("/")
    public String hello(Principal authUser) {
        log.info(authUser.toString());
        return "Hello from Auth Test Service!";
    }
}
