package com.example.myapplication.services;

import com.example.myapplication.model.response.UserResponse;
import com.example.myapplication.tokenManager.TokenManager;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UserService {
    String User = "user";

    String token = TokenManager.getToken();

    @GET(User)
    Call<UserResponse> getUsers(@Header("Authorization") String token, @Query("search") String search);
}
