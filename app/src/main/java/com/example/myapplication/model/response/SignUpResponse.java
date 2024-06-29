package com.example.myapplication.model.response;

public class SignUpResponse {
    private String message;
    private boolean onSuccess;

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
}
