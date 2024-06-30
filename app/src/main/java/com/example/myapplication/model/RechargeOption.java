package com.example.myapplication.model;

public class RechargeOption {
    private int price;
    private int imageResId;
    private int coins;

    public RechargeOption(int price, int imageResId, int coins) {
        this.price = price;
        this.imageResId = imageResId;
        this.coins = coins;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
