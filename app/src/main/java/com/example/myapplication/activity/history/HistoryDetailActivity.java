package com.example.myapplication.activity.history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.model.GameHistoryItem;
import com.example.myapplication.tokenManager.TokenManager;
import com.example.myapplication.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryDetailActivity extends AppCompatActivity {

    ImageButton btnBack;
    ImageView playerAvatar, opponentAvatar;
    CardView cvOpponent, cvPlayer;
    TextView txtPrice, txtDate, txtPlayerName, txtOpponentName, txtOpponentResult, txtPlayerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpView();
        loadData();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private  void setUpView(){
        btnBack = findViewById(R.id.backButton);
        playerAvatar = findViewById(R.id.imgPlayer);
        opponentAvatar = findViewById(R.id.imgOpponent);
        txtDate = findViewById(R.id.txtDate);
        txtOpponentName = findViewById(R.id.txtOpponentName);
        txtPrice = findViewById(R.id.txtPrice);
        txtPlayerName = findViewById(R.id.tvPlayerName);
        txtOpponentResult = findViewById(R.id.txtOpponentResult);
        txtPlayerResult = findViewById(R.id.txtPlayerResult);
        cvOpponent = findViewById(R.id.cvOpponent);
        cvPlayer = findViewById(R.id.cvPlayer);

    }

    private void loadData(){
        Intent intent = getIntent();

        String opponentName = intent.getStringExtra("name");
        String opponentPic = intent.getStringExtra("pic");
        double price = intent.getDoubleExtra("price", 0);
        String result = intent.getStringExtra("result");
        String createdAt = intent.getStringExtra("createdAt");

        txtOpponentName.setText(opponentName);
        txtPrice.setText(Double.toString(price));
        txtDate.setText(Utils.displayDate(createdAt));

        if(result != null && result.equalsIgnoreCase("lose")){
            txtPlayerResult.setText(result);
            txtPlayerResult.setTextColor(Color.parseColor("#B10909"));

            txtOpponentResult.setText("Win");
            txtOpponentResult.setTextColor(Color.parseColor("#3DA842"));

            cvOpponent.setCardBackgroundColor(Color.parseColor("#3DA842"));
            cvPlayer.setCardBackgroundColor(Color.parseColor("#B10909"));

        } else {
            txtPlayerResult.setText(result);
            txtPlayerResult.setTextColor(Color.parseColor("#3DA842"));

            txtOpponentResult.setText("Lose");
            txtOpponentResult.setTextColor(Color.parseColor("#B10909"));

            cvOpponent.setCardBackgroundColor(Color.parseColor("#B10909"));
            cvPlayer.setCardBackgroundColor(Color.parseColor("#3DA842"));

        }


        Picasso.get().load(opponentPic).into(opponentAvatar);

        try {
            JSONObject user = TokenManager.getUserObject();
            String userPic = user.getString("profilePic");

            Log.d("USERPIC", userPic);

            Picasso.get().load(userPic).into(playerAvatar);
            txtPlayerName.setText(user.getString("fullName"));

        } catch (JSONException e) {
            Log.d("ERRORHISTORYDETAIL", e.getMessage());
        }

    }
}