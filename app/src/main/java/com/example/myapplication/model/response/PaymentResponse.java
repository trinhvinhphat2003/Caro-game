package com.example.myapplication.model.response;

import com.example.myapplication.model.UserItem;

import java.util.List;

public class PaymentResponse {
   private String message;
   private PaymentData data;

   private Boolean onSuccess;

    public PaymentResponse(String message, PaymentData data, Boolean onSuccess) {
        this.message = message;
        this.data = data;
        this.onSuccess = onSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentData getData() {
        return data;
    }

    public void setData(PaymentData data) {
        this.data = data;
    }

    public Boolean getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(Boolean onSuccess) {
        this.onSuccess = onSuccess;
    }

    private class PaymentData {

       private int wallet;

        public PaymentData(int wallet) {
            this.wallet = wallet;
        }

        public int getWallet() {
            return wallet;
        }

        public void setWallet(int wallet) {
            this.wallet = wallet;
        }
    }
}
