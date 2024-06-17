package com.example.myapplication.model.response;

import com.example.myapplication.model.UserItem;

import java.util.List;

public class UserResponse {
    private String message;
    private List<UserItem> data;
    private boolean onSuccess;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserItem> getData() {
        return data;
    }

    public void setData(List<UserItem> data) {
        this.data = data;
    }

    public boolean isOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(boolean onSuccess) {
        this.onSuccess = onSuccess;
    }
}
