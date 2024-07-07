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
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class GameActivity extends AppCompatActivity {
    private Context context;
    final static int boardSize = 12;
    private ImageView[][] boardView = new ImageView[boardSize][boardSize];
    private String[][] boardValue = new String[boardSize][boardSize];
    private Drawable[] drawableCell = new Drawable[6]; //0: empty,1: player 1, 2: player 2, 3: background

    private JSONObject room;
    private String playerTurn = "";
    private String currentTurnUser;
    private String gameStatus = "created";
    private int gamePrice = 0;

    public SocketManager socketManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = this;
        socketManager = SocketManager.getInstance(TokenManager.getId_user());
        socketManager.connect();
        gameStatus = "ready";
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
        setPlayerView();
        initGame();
        createLeavingRoom();

        setupListeners();
    }

    private void loadResources() {
        drawableCell[0] = null;//empty cell
        drawableCell[1] = context.getDrawable(R.drawable.fire);//player 1
        drawableCell[2] = context.getDrawable(R.drawable.droplet);//player 2
        drawableCell[3] = context.getDrawable(R.drawable.cell_bg);//background
        drawableCell[4] = context.getDrawable(R.drawable.red_cell_bg);//winning X background
        drawableCell[5] = context.getDrawable(R.drawable.blue_cell_bg);//winning O background
    }

    private void setPlayerView() {
        TextView playerName;
        TextView playerWallet;
        ImageView playerAvatar;
        ImageView playerSymbol;
        TextView playerTurnText;

        playerName = findViewById(R.id.playerName);
        playerWallet = findViewById(R.id.playerWallet);
        playerAvatar = findViewById(R.id.playerAvatar);
        playerSymbol = findViewById(R.id.playerSymbol);
        playerTurnText = findViewById(R.id.playerTurnText);
        JSONObject currentUserData;

        try {
            gamePrice = room.getInt("gamePrice");

            String turn = room.getString("turn");

            JSONObject playerX = room.getJSONObject("playerX");
            JSONObject playerY = room.getJSONObject("playerO");
            currentUserData = turn.equals(playerX.getString("_id")) ? playerX : playerY;

            playerName.setText(currentUserData.getString("fullName"));

            String imageUrl = currentUserData.getString("profilePic");
            Picasso.get().load(imageUrl).into(playerAvatar);

            String currentplayerSymbol = turn.equals(playerX.getString("_id")) ? "X" : "O";
            playerSymbol.setImageDrawable(drawableCell[convertSymbolToIndex(currentplayerSymbol)]);

            playerTurnText.setText(
                    currentUserData.getString("_id").equals(TokenManager.getId_user())
                            ? "Your turn"
                            : "Opponent's turn"
            );

            playerWallet.setText(currentUserData.getString("wallet") + " coins");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
        if(gameStatus.equals("finish")){
            return;
        }
        if(!boardValue[x][y].isEmpty()){
            return;
        }
        if(currentTurnUser != null && !currentTurnUser.equals(TokenManager.getId_user())){
            return;
        }
        JSONObject data = new JSONObject();
        try {
            String user = TokenManager.getId_user();
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
        mSocket.on("finish-game", onFinishGame);
    }

    private Emitter.Listener onMove = args -> {
        JSONObject data = (JSONObject) args[0];
        room = data;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray gameArray = data.getJSONArray("game");
                    JSONObject playerX = data.getJSONObject("playerX");
                    currentTurnUser = data.getString("turn");
                    playerTurn = playerX.getString("_id").equals(TokenManager.getId_user()) ? "X" : "O";
                    setPlayerView();
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

    private Emitter.Listener onFinishGame = args -> {
        JSONObject data = (JSONObject) args[0];
        TextView playerTurnText;
        playerTurnText = findViewById(R.id.playerTurnText);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameStatus = "finish";
                try {
                    String winnerId = data.getString("winner");
                    Boolean isWinner = winnerId.equals(TokenManager.getId_user());

                    JSONObject user = TokenManager.getUserObject();
                    user.put("wallet", String.valueOf(Integer.valueOf((String)user.get("wallet")).intValue()
                            + (isWinner ? gamePrice: -gamePrice))
                    );
                    System.out.println("finish game");
                    playerTurnText.setText(
                            (isWinner ? "You win" : "You lose")
                                    + " ("
                                    + (isWinner ? "+" : "-")
                                    + gamePrice + " coins"
                                    + ")"
                    );
                    System.out.println("finish game 1 " );
                    JSONArray winningCells = data.getJSONObject("result").getJSONArray("cells");
                    System.out.println("finish game 2 " );
                    String winningMark = data.getJSONObject("result").getString("mark");


                    for (int i = 0; i < winningCells.length(); i++) {
                        JSONArray winningCell = winningCells.getJSONArray(i);
                        int x = winningCell.getInt(0);
                        int y = winningCell.getInt(1);
                        boardView[x][y].setBackground(winningMark.equals("X") ? drawableCell[4] : drawableCell[5]);
                    }

//                    playerName.setText(winner.getString("fullName"));
//                    playerWallet.setText(winner.getString("wallet") + " coins");
//
//                    String imageUrl = winner.getString("profilePic");
//                    Picasso.get().load(imageUrl).into(playerAvatar);

                    Toast.makeText(context,
                            (isWinner ? "You get" : "You lose") + " " + gamePrice + " coins",
                            Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    };

    private void createLeavingRoom() {
        LinearLayout leaveRoomButton = findViewById(R.id.leaveRoomButton);
        leaveRoomButton.setOnClickListener(v -> {
            socketManager.getmSocket().disconnect();
            Intent intent = new Intent(this ,MainActivity.class);
            startActivity(intent);
            startActivity(intent);
            finish();
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.disconnect();
        }
    }
}