package com.example.myapplication.socket;

import com.example.myapplication.model.Message;

public interface SocketCallback {
    void onMessageReceived(Message data);
}
