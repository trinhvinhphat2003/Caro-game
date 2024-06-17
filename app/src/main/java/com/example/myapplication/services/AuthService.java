package com.example.myapplication.services;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.example.myapplication.model.request.LoginRequest;
import com.example.myapplication.model.response.LoginResponse;

public interface AuthService {
    String Auth = "auth";


    @POST (Auth + "/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
