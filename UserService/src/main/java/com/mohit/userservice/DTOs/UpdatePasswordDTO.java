package com.mohit.userservice.DTOs;

public record UpdatePasswordDTO(String email, String oldPassword, String newPassword) { }