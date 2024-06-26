package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.socket.SocketManager;
import com.example.myapplication.tokenManager.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class GameActivity extends AppCompatActivity {
    private Context context;
    final static int boardSize = 12;
    private ImageView[][] boardView = new ImageView[boardSize][boardSize];
    private String[][] boardValue = new String[boardSize][boardSize];
    private Drawable[] drawableCell = new Drawable[4]; //0: empty,1: player 1, 2: player 2, 3: background

    private JSONObject room;
    private String playerTurn = "";
    private String currentTurnUser;

    public SocketManager socketManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        context = this;
        socketManager = SocketManager.getInstance(TokenManager.getId_user());
        socketManager.connect();

        Intent intent = getIntent();
        String roomString = intent.getStringExtra("room");
        try {
            room = new JSONObject(roomString);
            JSONArray gameArray = room.getJSONArray("game");
            JSONObject playerX = room.getJSONObject("playerX");
            currentTurnUser = room.getString("turn");
            playerTurn = playerX.getString("_id").equals(TokenManager.getId_user()) ? "X" : "O";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadResources();
        initGame();

        setupListeners();
    }

    private void loadResources() {
        drawableCell[0] = null;//empty cell
        drawableCell[1] = context.getDrawable(R.drawable.fire);//player 1
        drawableCell[2] = context.getDrawable(R.drawable.droplet);//player 2
        drawableCell[3] = context.getDrawable(R.drawable.cell_bg);//background
    }
    private void initGame() {
        int sizeOfCell = Math.round(calculateScreenWidth()/boardSize);
        LinearLayout.LayoutParams lpRow  = new LinearLayout.LayoutParams(sizeOfCell*boardSize, sizeOfCell);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeOfCell, sizeOfCell);

        LinearLayout boardLayout = findViewById(R.id.board);

        for(int x = 0; x < boardSize ; x++ ){
            LinearLayout linRow = new LinearLayout(context);
            for(int y = 0; y < boardSize; y++){
                boardView[x][y] = new ImageView(context);
                boardView[x][y].setPadding(6,6,6,6);
                boardView[x][y].setBackground(drawableCell[3]);
                boardValue[x][y] = "";
                int finalX = x;
                int finalY = y;
                boardView[x][y].setOnClickListener(v -> {
                    makeMove(finalX, finalY);
                });
                linRow.addView(boardView[x][y], lpCell);
            }
            boardLayout.addView(linRow, lpRow);
        }
    }



    private float calculateScreenWidth() {
        Resources resources = context.getResources();//ok
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    private void makeMove(int x, int y) {
        Log.d("Make move at: ", boardValue[x][y]);
        if(!boardValue[x][y].isEmpty()){
            return;
        }
        if(currentTurnUser != null && !currentTurnUser.equals(TokenManager.getId_user())){
            return;
        }
        JSONObject data = new JSONObject();
        try {
            JSONObject user = TokenManager.getUserObject();
            data.put("user", user).put("row", x).put("col", y);
        } catch (JSONException e){
            throw new RuntimeException(e);

        }
        boardView[x][y].setImageDrawable(drawableCell[convertSymbolToIndex(playerTurn)]);
        socketManager.getmSocket().emit("move", data);
    }

    private int convertSymbolToIndex(String symbol){
        if(symbol.equals("X")) {
            return 1;
        }
        if(symbol.equals("O")) {
            return 2;
        }
        return 0;
    }


    public void setupListeners() {
        Socket mSocket = socketManager.getmSocket();
        mSocket.on("move", onMove);
    }

    private Emitter.Listener onMove = args -> {
        JSONObject data = (JSONObject) args[0];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray gameArray = data.getJSONArray("game");
                    JSONObject playerX = data.getJSONObject("playerX");
                    currentTurnUser = data.getString("turn");
                    playerTurn = playerX.getString("_id").equals(TokenManager.getId_user()) ? "X" : "O";

                    for (int x = 0; x < gameArray.length(); x++) {
                        JSONArray rowArray = gameArray.getJSONArray(x);
                        for (int y = 0; y < rowArray.length(); y++) {
                            String cellValue = rowArray.getString(y);
                            boardValue[x][y] = cellValue;
                            Log.d("Cell value: ", cellValue);
                            boardView[x][y].setImageDrawable(drawableCell[convertSymbolToIndex(cellValue)]);
                            int finalX = x, finalY = y;
                            boardView[x][y].setOnClickListener(v -> {
                                makeMove(finalX, finalY);
                            });
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    };
}