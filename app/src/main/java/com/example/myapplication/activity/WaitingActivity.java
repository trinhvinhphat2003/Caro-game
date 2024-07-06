package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.socket.NotificationHelper;
import com.example.myapplication.socket.SocketManager;
import com.example.myapplication.tokenManager.TokenManager;

public class WaitingActivity extends AppCompatActivity {

    private Button cancelWaitingBtn;
    private SocketManager socketManager;

    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting);
        notificationHelper = new NotificationHelper(this);
        socketManager = SocketManager.getInstance(TokenManager.getId_user());
        socketManager.connect();
        notificationHelper.listenForNotification(socketManager);

        cancelWaitingBtn = findViewById(R.id.cancelWaitingBtn);
        cancelWaitingBtn.setOnClickListener(v -> {
            SocketManager.getInstance(TokenManager.getId_user()).disconnect();
            Intent intent = new Intent(this ,MainActivity.class);
            startActivity(intent);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}