package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.adapter.GameHistoryAdapter;
import com.example.myapplication.api.GameHistoryRepository;
import com.example.myapplication.model.GameHistoryItem;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.response.GameHistoryResponse;
import com.example.myapplication.model.response.MessageReponse;
import com.example.myapplication.services.GameService;
import com.example.myapplication.socket.NotificationHelper;

import com.example.myapplication.tokenManager.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private GameService gameService;
    private ImageView avatar;
    private Button btnLogout;
    private TextView txtUsername, coinDisplay;
    private RecyclerView rvHistory;
    private JSONObject user = TokenManager.getUserObject();
    private List<GameHistoryItem> gameHistoryItemList;
    private GameHistoryAdapter GHAdapter;

    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        notificationHelper = new NotificationHelper(this);

        ImageButton backButton = findViewById(R.id.backButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Finish the activity to go back
            }
        });

        rvHistory = findViewById(R.id.rvHistory);
        avatar = findViewById(R.id.avatar);
        btnLogout = findViewById(R.id.btnLogout);
        txtUsername = findViewById(R.id.txtUsername);
        coinDisplay = findViewById(R.id.coinDisplay);

        gameService = GameHistoryRepository.getGameService();

        loadUserData();
        bindRecyclerView();
        loadGameHistory();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutAndNavigateToLogin();
            }
        });

    }

    private void loadUserData() {
        StringBuilder coinDisplayTxt = new StringBuilder("Coins: ");
        try {

            txtUsername.setText(user.getString("fullName"));

            Log.d("USER", user.toString());
            String imageUrl = user.getString("profilePic");

            Picasso.get().load(imageUrl).into(avatar);
            coinDisplayTxt.append(String.valueOf(user.get("wallet")));
            coinDisplay.setText(coinDisplayTxt);
        } catch (JSONException e) {
            //throw new RuntimeException(e);
        }
    }

    private void bindRecyclerView(){
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        gameHistoryItemList = new ArrayList<>();
        GHAdapter = new GameHistoryAdapter(gameHistoryItemList);
        rvHistory.setAdapter(GHAdapter);

    }

    private void loadGameHistory(){



        try {
            Call<GameHistoryResponse> call = gameService.getGameHistory("Bearer " + TokenManager.getToken());

            call.enqueue(new Callback<GameHistoryResponse>() {
                @Override
                public void onResponse(Call<GameHistoryResponse> call, Response<GameHistoryResponse> response) {
                    if (response.body() != null) {
                        GameHistoryResponse gameHistoryResponse = response.body();

                        if (gameHistoryResponse.isOnSuccess()) {

                            List<GameHistoryItem> historyList = gameHistoryResponse.getData();
                            Log.d("GAMEDATA", historyList.toString());
                            for (int i = 0; i < 5 && i < historyList.size(); i++) {
                                GameHistoryItem history = historyList.get(i);
                                gameHistoryItemList.add(new GameHistoryItem(history.getPrice(), history.getResult(), history.getCompetitor(), history.getCreatedAt()));
                            }
                            GHAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onFailure(Call<GameHistoryResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void logOutAndNavigateToLogin() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}