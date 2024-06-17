package com.example.myapplication.api;

import com.example.myapplication.services.AuthService;

public class AuthRespository {
    public static AuthService getAuthService(){
        return APIClient.getClient().create(AuthService.class);
    }
}
