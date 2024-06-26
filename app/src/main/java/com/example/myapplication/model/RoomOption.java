package com.example.myapplication.model;

public class RoomOption {
    private int imageResId;
    private int coins;

    public RoomOption(int imageResId, int coins) {
        this.imageResId = imageResId;
        this.coins = coins;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getCoins() {
        return coins;
    }
}
