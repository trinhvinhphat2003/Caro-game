package com.example.myapplication.activity.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.ProfileActivity;
import com.example.myapplication.adapter.GameHistoryAdapter;
import com.example.myapplication.adapter.RecyclerViewInterface;
import com.example.myapplication.api.GameHistoryRepository;
import com.example.myapplication.model.GameHistoryItem;
import com.example.myapplication.model.response.GameHistoryResponse;
import com.example.myapplication.services.GameService;
import com.example.myapplication.tokenManager.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ImageButton btnBack;
    private List<GameHistoryItem> gameHistoryItemList;
    private GameService gameService;
    private GameHistoryAdapter GHAdapter;
    private RecyclerView rvHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.backButton);
        rvHistory = findViewById(R.id.rvHistory);
        gameService = GameHistoryRepository.getGameService();

        bindRecyclerView();
        loadGameHistory();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void bindRecyclerView(){
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        gameHistoryItemList = new ArrayList<>();
        GHAdapter = new GameHistoryAdapter(gameHistoryItemList, this);
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
                            for (GameHistoryItem history : historyList) {
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

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getApplicationContext(), HistoryDetailActivity.class);

        GameHistoryItem item =  gameHistoryItemList.get(position);
        String opponentName = item.getCompetitor().getFullName();
        String opponentPic = item.getCompetitor().getProfilePic();
        double price = item.getPrice();
        String result = item.getResult();
        String createdAt = item.getCreatedAt();

        intent.putExtra("name", opponentName);
        intent.putExtra("pic", opponentPic);
        intent.putExtra("price", price);
        intent.putExtra("result", result);
        intent.putExtra("createdAt", createdAt);

        startActivity(intent);

    }
}