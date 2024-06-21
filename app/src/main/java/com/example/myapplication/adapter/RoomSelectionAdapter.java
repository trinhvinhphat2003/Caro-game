package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.RoomOption;

import java.util.List;

public class RoomSelectionAdapter extends RecyclerView.Adapter<RoomSelectionAdapter.ViewHolder> {
    private List<RoomOption> roomOptions;
    public RoomSelectionAdapter(List<RoomOption> roomOptions) {
        this.roomOptions = roomOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomOption option = roomOptions.get(position);
        holder.coinsTextView.setText(option.getCoins());
        holder.imageView.setImageResource(option.getImageResId());
    }

    @Override
    public int getItemCount() {
        return roomOptions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView coinsTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            coinsTextView = itemView.findViewById(R.id.coinsTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
