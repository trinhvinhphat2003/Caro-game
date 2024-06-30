package com.example.myapplication.services;



import com.example.myapplication.model.request.PaymentRequest;
import com.example.myapplication.model.response.PaymentResponse;
import com.example.myapplication.tokenManager.TokenManager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PaymentService {
    String Order = "order";


    @POST (Order)
    Call<PaymentResponse> createPayment(@Header("Authorization") String token, @Body PaymentRequest paymentRequest);
}
