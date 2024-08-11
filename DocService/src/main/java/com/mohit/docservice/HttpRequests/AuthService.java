package com.mohit.docservice.HttpRequests;

import com.mohit.docservice.DTOs.AuthRequestDTO;
import com.mohit.docservice.DTOs.AuthResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/auth/authenticateToken")
    public Call<AuthResponseDTO> authenticateToken(@Body AuthRequestDTO authRequestDTO);
}
