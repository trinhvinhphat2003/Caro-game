package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.RechargeOption;

import java.util.List;

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
        holder.priceTextView.setText(option.getPrice());
        holder.coinsTextView.setText(option.getCoins());
        holder.imageView.setImageResource(option.getImageResId());
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