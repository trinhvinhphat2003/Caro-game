package com.example.myapplication.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.api.PaymentRepository;
import com.example.myapplication.api.UserRepository;
import com.example.myapplication.model.RechargeOption;
import com.example.myapplication.model.request.LoginRequest;
import com.example.myapplication.model.request.PaymentRequest;
import com.example.myapplication.model.response.LoginResponse;
import com.example.myapplication.model.response.PaymentResponse;
import com.example.myapplication.model.response.UserResponse;
import com.example.myapplication.services.PaymentService;
import com.example.myapplication.services.UserService;
import com.example.myapplication.tokenManager.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder> {
    private List<RechargeOption> rechargeOptions;

    public RechargeAdapter(List<RechargeOption> rechargeOptions) {
        this.rechargeOptions = rechargeOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RechargeOption option = rechargeOptions.get(position);
        holder.priceTextView.setText(option.getPrice() + " VND");
        holder.coinsTextView.setText(option.getCoins() + " coins");
        holder.imageView.setImageResource(option.getImageResId());
        holder.itemView.setOnClickListener(v -> {
            PaymentService paymentService = PaymentRepository.getPaymentService();
            PaymentRequest paymentResquest = new PaymentRequest(option.getPrice());

            try {
                Call<PaymentResponse> call = paymentService.createPayment("Bearer " + TokenManager.getToken(), paymentResquest);
                call.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                        if (response.body() != null){
                            PaymentResponse paymentResponse = response.body();
                            if (paymentResponse.getOnSuccess()) {

                            } else {

                            }
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {

                    }
                });
            } catch (Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return rechargeOptions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView priceTextView;
        TextView coinsTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            coinsTextView = itemView.findViewById(R.id.coinsTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}