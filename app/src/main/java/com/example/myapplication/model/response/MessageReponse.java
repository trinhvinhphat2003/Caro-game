package com.example.myapplication.model.response;

import java.util.List;

public class MessageReponse {
    private String message;
    private boolean onSuccess;
    private List<Message> data;


    public static class Message {
        private String message;
        private String senderId;
        private String receiverId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }
    }

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

    public List<Message> getData() {
        return data;
    }

    public void setData(List<Message> data) {
        this.data = data;
    }
}
