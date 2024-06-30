package com.example.myapplication.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.GameActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.WaitingActivity;
import com.example.myapplication.model.RoomOption;
import com.example.myapplication.socket.SocketManager;
import com.example.myapplication.tokenManager.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RoomSelectionAdapter extends RecyclerView.Adapter<RoomSelectionAdapter.ViewHolder> {
    private List<RoomOption> roomOptions;
    private Context context;
    private Dialog dialog;
    public SocketManager socketManager;
    public RoomSelectionAdapter(List<RoomOption> roomOptions, Dialog dialog) {
        this.context = dialog.getContext();
        this.dialog = dialog;
        this.roomOptions = roomOptions;
        socketManager = SocketManager.getInstance(TokenManager.getId_user());
        socketManager.connect();

        setupListeners();
    }

    public RoomSelectionAdapter(List<RoomOption> roomOptions, Context context) {
        this.context = context;
        this.roomOptions = roomOptions;
        socketManager = SocketManager.getInstance(TokenManager.getId_user());
        socketManager.connect();

        setupListeners();
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
        holder.coinsTextView.setText(option.getCoins() + " coins");
        holder.imageView.setImageResource(option.getImageResId());
        holder.itemView.setOnClickListener(v -> {
            try {
                joinRoom(option.getCoins());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView coinsTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            coinsTextView = itemView.findViewById(R.id.coinsTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    private void joinRoom(int money) throws JSONException {
        JSONObject data = new JSONObject();
        JSONObject user = TokenManager.getUserObject();
        data.put("user", user);
        data.put("money", money);
        Toast.makeText(context, "Joining...", Toast.LENGTH_SHORT).show();
        socketManager.getmSocket().emit("joinroom", data);
    }

    public void setupListeners() {
        Socket mSocket = socketManager.getmSocket();
        mSocket.on("joinroom-success", onJoinRoomSuccess);
        mSocket.on("waiting-play", onWaitingPlay);
        mSocket.on("emely-scrare", onEmelyScare);
        mSocket.on("not-enough-money", onNotEnoughMoney);
    }

    private Emitter.Listener onJoinRoomSuccess = args -> {
        JSONObject data = (JSONObject) args[0];
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("room", data.toString());
        dialog.dismiss();
        context.startActivity(intent);

        showToast("Join room successfully");
    };

    private Emitter.Listener onWaitingPlay = args -> {
        JSONObject data = (JSONObject) args[0];
        showToast("Waiting Play");
        Intent intent = new Intent(context, WaitingActivity.class);
//        intent.putExtra("room", data.toString());
        dialog.dismiss();
        context.startActivity(intent);
    };

    private Emitter.Listener onEmelyScare = args -> {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        showToast("Your opponent has quit");
    };

    private Emitter.Listener onNotEnoughMoney = args -> {
        showToast("Not enough money");
    };

    private void showToast(String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Background work here
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

}