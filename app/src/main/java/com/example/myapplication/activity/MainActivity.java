package com.example.myapplication.activity;



import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.fragment.home.HomeFragment;
import com.example.myapplication.fragment.map.MapFragment;
import com.example.myapplication.fragment.message.MessageFragment;
import com.example.myapplication.model.Message;
import com.example.myapplication.socket.SocketManager;
import com.example.myapplication.tokenManager.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "notification_channel_id";

//    Button toLogin;
    ActivityMainBinding binding;
    private SocketManager socketManager;
    private Message receivedMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        createNotificationChannel();
        socketManager = new SocketManager(TokenManager.getId_user());
        socketManager.connect();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId =  item.getItemId();
            if(itemId == R.id.homeTab) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.messageTab) {
                replaceFragment(new MessageFragment());
            } else if (itemId == R.id.mapTab) {
                replaceFragment(new MapFragment());
            }
            return true;
        });
        socketManager.getmSocket().on("newMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String message = data.getString("message");
                    receivedMessage = new Message(message, false);
                    String idSender = data.getString("senderId");
                    String senderName = data.getString("senderName");

                    runOnUiThread(() -> {
                        sendNotification(receivedMessage.getContent(), idSender, senderName);
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


//        toLogin = findViewById(R.id.toLogin);
//        toLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.disconnect();
        }
    }
    private void sendNotification(String message, String idSender, String senderName) {


        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(senderName+": ")
                .setContentText(message)
                .setLargeIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(idSender), builder.build());
            Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Notification Manager is null", Toast.LENGTH_SHORT).show();
        }
    }

    private int getNotificationId(String idSender) {
        int hash = idSender.hashCode();
        return hash;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

}