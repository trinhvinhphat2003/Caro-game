package com.example.myapplication.api;

import com.example.myapplication.services.UserService;

public class UserRepository {
    public static UserService getUserService(){
        return APIClient.getClient().create(UserService.class);
    }
}
