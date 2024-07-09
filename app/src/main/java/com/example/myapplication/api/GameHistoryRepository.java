package com.example.myapplication.api;


import com.example.myapplication.services.GameService;

public class GameHistoryRepository {
    public static GameService getGameService(){
        return APIClient.getClient().create(GameService.class);
    }
}
