package com.example.myapplication.model;

public class Message {
    private String content;
    private boolean isMine; // Whether the message is sent by me or the other person

    public Message(String content, boolean isMine) {
        this.content = content;
        this.isMine = isMine;
    }

    public String getContent() {
        return content;
    }

    public boolean isMine() {
        return isMine;
    }
}
