package com.example.myapplication.services;

import com.example.myapplication.model.request.MessageRequest;
import com.example.myapplication.model.response.MessageReponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
    String Message = "message";

    @GET(Message+"/{id}")
    Call<MessageReponse> getMessages(@Path("id") String id,  @Header("Authorization") String token);
    @POST(Message + "/send/{id}")
    Call<MessageReponse> sendMessage(@Path("id") String id, @Header("Authorization") String token, @Body MessageRequest messageRequest);


}
