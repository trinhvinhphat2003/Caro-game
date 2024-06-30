package com.example.myapplication.api;

import com.example.myapplication.services.PaymentService;

public class PaymentRepository {
    public static PaymentService getPaymentService(){
        return APIClient.getClient().create(PaymentService.class);
    }
}
