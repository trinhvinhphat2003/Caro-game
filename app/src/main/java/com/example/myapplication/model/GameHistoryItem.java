package com.example.myapplication.model;

import java.util.Date;

public class GameHistoryItem {

    private double price;
    private String result;
    private UserItem competitor;
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public GameHistoryItem(double price, String result, UserItem competitor, String createdAt) {
        this.price = price;
        this.result = result;
        this.competitor = competitor;
        this.createdAt = createdAt;
    }

    public double getPrice() {
        return price;
    }

    public String getResult() {
        return result;
    }

    public UserItem getCompetitor() {
        return competitor;
    }
}
