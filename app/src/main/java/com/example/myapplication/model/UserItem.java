package com.example.myapplication.model;

public class UserItem {
    private String name;
    private String lastMessage;
    private int profileImage;

    public UserItem(String name, String lastMessage, int profileImage) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getProfileImage() {
        return profileImage;
    }
}

