package com.example.myapplication.services;

import com.example.myapplication.model.response.GameHistoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GameService {

    String Game = "game";

    @GET(Game)
    Call<GameHistoryResponse> getGameHistory(@Header("Authorization") String token);


}
