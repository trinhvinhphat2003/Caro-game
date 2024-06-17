package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.activity.ChatActivity;
import com.example.myapplication.model.UserItem;

import java.util.List;
import com.example.myapplication.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserItem> userList;
    private Context context;

    public UserAdapter(List<UserItem> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserItem currentItem = userList.get(position);
        holder.userNameTextView.setText(currentItem.getFullName());

        // Load image using Glide
        Glide.with(context)
                .load(currentItem.getProfilePic())
                .placeholder(R.drawable.anonymous)
                .error(R.drawable.anonymous)
                .into(holder.profileImageView);

        holder.itemView.setOnClickListener(v -> {
            // Handle item click event
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("idReceiver", currentItem.getId());
            intent.putExtra("fullName", currentItem.getFullName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView userNameTextView;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
        }
    }
}

