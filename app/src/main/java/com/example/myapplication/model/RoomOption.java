package com.example.myapplication.model;

public class RoomOption {
    private int imageResId;
    private String coins;

    public RoomOption(int imageResId, String coins) {
        this.imageResId = imageResId;
        this.coins = coins;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getCoins() {
        return coins;
    }
}
