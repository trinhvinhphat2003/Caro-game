package com.example.myapplication.model.response;

import com.example.myapplication.model.GameHistoryItem;

import java.util.List;

public class GameHistoryResponse {

    private String message;
    private boolean onSuccess;
    private List<GameHistoryItem> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(boolean onSuccess) {
        this.onSuccess = onSuccess;
    }

    public List<GameHistoryItem> getData() {
        return data;
    }

    public void setData(List<GameHistoryItem> data) {
        this.data = data;
    }
}
