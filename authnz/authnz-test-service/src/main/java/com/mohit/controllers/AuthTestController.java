package com.mohit.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-test")
public class AuthTestController {
    @GetMapping("/")
    public String hello() {
        return "Hello from Auth Test Service!";
    }
}
