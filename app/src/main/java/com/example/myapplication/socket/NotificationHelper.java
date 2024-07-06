package com.example.myapplication.socket;
import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class NotificationHelper extends AppCompatActivity {
    private static final String CHANNEL_ID = "notification_channel_id";
    private Context context;
    private Message receivedMessage;

    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public void sendNotification(String message, String idSender, String senderName) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(senderName + ": ")
                .setContentText(message)
                .setLargeIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(idSender), builder.build());
            Toast.makeText(context, "Notification sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Notification Manager is null", Toast.LENGTH_SHORT).show();
        }
    }

    private int getNotificationId(String idSender) {
        return idSender.hashCode();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notifications", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    public void listenForNotification(SocketManager socket) {
        socket.getmSocket().on("newMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String message = data.getString("message");
                    receivedMessage = new Message(message, false);
                    String idSender = data.getString("senderId");
                    String senderName = data.getString("senderName");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendNotification(receivedMessage.getContent(), idSender, senderName);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
