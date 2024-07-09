package com.example.myapplication.adapter;

import android.graphics.Color;
import android.util.Log;
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

    private final RecyclerViewInterface recyclerViewInterface;
    private List<GameHistoryItem> history;

    public GameHistoryAdapter(List<GameHistoryItem> history, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.history = history;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.item_match_history, parent, false);

        return new GameHistoryViewHolder(view, recyclerViewInterface);

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

    public static class GameHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtMatchResult;
        private ImageView imgAvatar;

        public GameHistoryViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPlayerName);
            txtMatchResult = itemView.findViewById(R.id.txtMatchResult);
            imgAvatar = itemView.findViewById(R.id.imgPlayerAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RecylerClick", "clicked 1");

                    if(recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            Log.d("RecylerClick", "clicked 3");
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
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
