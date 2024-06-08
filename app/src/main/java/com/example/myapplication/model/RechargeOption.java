package com.example.myapplication.model;

public class RechargeOption {
    private String price;
    private int imageResId;
    private String coins;

    public RechargeOption(String price, int imageResId, String coins) {
        this.price = price;
        this.imageResId = imageResId;
        this.coins = coins;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getCoins() {
        return coins;
    }
}
