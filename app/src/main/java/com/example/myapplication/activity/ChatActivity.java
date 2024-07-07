package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.api.MessageRepository;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.request.MessageRequest;
import com.example.myapplication.model.response.MessageReponse;
import com.example.myapplication.services.MessageService;
import com.example.myapplication.socket.NotificationHelper;
import com.example.myapplication.socket.SocketManager;
import com.example.myapplication.tokenManager.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private List<Message> messages;
    private ChatAdapter adapter;
    private TextView textViewFullName;
    private Message receivedMessage;

    MessageService messageService;
    private SocketManager socketManager;

    private NotificationHelper notificationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Finish the activity to go back
            }
        });

        notificationHelper = new NotificationHelper(this);

        messageService = MessageRepository.getMessageService();
        Intent intent = getIntent();
        String idReceiver = intent.getStringExtra("idReceiver");
        String fullName = intent.getStringExtra("fullName");
        socketManager = SocketManager.getInstance(TokenManager.getId_user());
        socketManager.connect();

        socketManager.getmSocket().on("newMessage", new Emitter.Listener() {
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

                            if (idSender.equals(idReceiver)){
                                messages.add(receivedMessage);
                                adapter.notifyDataSetChanged();
                            }

                            notificationHelper.sendNotification(receivedMessage.getContent(), idSender, senderName);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




        // Initialize views
        recyclerViewMessages = findViewById(R.id.messageRecyclerView);
        editTextMessage = findViewById(R.id.messageInputEditText);
        buttonSendMessage = findViewById(R.id.sendButton);
        textViewFullName = findViewById(R.id.titleTextView);
        textViewFullName.setText(idReceiver + " - " + fullName);

        // Initialize RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);
        recyclerViewMessages.setAdapter(adapter);

        // Send button click listener
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = editTextMessage.getText().toString().trim();
                if (!messageContent.isEmpty()) {
                    messages.add(new Message(messageContent, true)); // Add message sent by user
                    adapter.notifyDataSetChanged(); // Refresh RecyclerView
                    MessageRequest messageRequest = new MessageRequest(messageContent);
                    messageService.sendMessage(idReceiver, "Bearer " + TokenManager.getToken(), messageRequest).enqueue(new Callback<MessageReponse>() {
                        @Override
                        public void onResponse(Call<MessageReponse> call, Response<MessageReponse> response) {

                        }

                        @Override
                        public void onFailure(Call<MessageReponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                    editTextMessage.setText(""); // Clear input field


                }

            }
        });

        // Add mock messages
        addMockMessages(idReceiver);
    }

    // Add mock messages for testing
    private void addMockMessages(String idReceiver) {
        try {
            Call<MessageReponse> call = messageService.getMessages(idReceiver, "Bearer " + TokenManager.getToken());

            call.enqueue(new Callback<MessageReponse>() {
                @Override
                public void onResponse(Call<MessageReponse> call, Response<MessageReponse> response) {
                    if (response.body() != null) {
                        MessageReponse messageReponse = response.body();
                        if (messageReponse.isOnSuccess()) {
                            List<MessageReponse.Message> messageList = messageReponse.getData();
                            for (MessageReponse.Message message : messageList) {
                                messages.add(new Message(message.getMessage(), !message.getSenderId().equals(idReceiver)));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageReponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}