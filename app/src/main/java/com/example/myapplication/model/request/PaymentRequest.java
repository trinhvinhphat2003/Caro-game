package com.example.myapplication.model.request;

public class PaymentRequest {
    private int money;

    public PaymentRequest(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
