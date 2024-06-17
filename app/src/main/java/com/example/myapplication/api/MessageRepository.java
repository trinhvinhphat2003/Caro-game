package com.example.myapplication.api;

import com.example.myapplication.services.MessageService;

public class MessageRepository {
    public static MessageService getMessageService(){
        return APIClient.getClient().create(MessageService.class);
    }
}
