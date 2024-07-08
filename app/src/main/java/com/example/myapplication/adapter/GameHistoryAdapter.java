package com.example.myapplication.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.model.GameHistoryItem;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.UserItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

public class GameHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<GameHistoryItem> history;

    public GameHistoryAdapter(List<GameHistoryItem> history) {
        this.history = history;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.item_match_history, parent, false);
        return new GameHistoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameHistoryItem matchHistory = history.get(position);
            ((GameHistoryViewHolder) holder).bind(matchHistory);

    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    static class GameHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtMatchResult;
        private ImageView imgAvatar;

        GameHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPlayerName);
            txtMatchResult = itemView.findViewById(R.id.txtMatchResult);
            imgAvatar = itemView.findViewById(R.id.imgPlayerAvatar);
        }

        void bind(GameHistoryItem gameHistoryItem) {

                UserItem opponent = gameHistoryItem.getCompetitor();
                String result = gameHistoryItem.getResult();

                txtName.setText(opponent.getFullName());

                txtMatchResult.setText(result);
                if(result.equalsIgnoreCase("lose")){
                    txtMatchResult.setTextColor(Color.parseColor("#B10909"));
                } else {
                    txtMatchResult.setTextColor(Color.parseColor("#3DA842"));
                }

                String imageUrl = opponent.getProfilePic();
                Picasso.get().load(imageUrl).into(imgAvatar);

        }
    }

}
