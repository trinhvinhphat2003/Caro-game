package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private List<Message> messages;
    private ChatAdapter adapter;

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
        // Initialize views
        recyclerViewMessages = findViewById(R.id.messageRecyclerView);
        editTextMessage = findViewById(R.id.messageInputEditText);
        buttonSendMessage = findViewById(R.id.sendButton);

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
                    editTextMessage.setText(""); // Clear input field
                    // Here you can implement logic to send the message to the other person
                }
            }
        });

        // Add mock messages
        addMockMessages();
    }

    // Add mock messages for testing
    private void addMockMessages() {
        messages.add(new Message("Hello", false));
        messages.add(new Message("Hi there!", true));
        messages.add(new Message("How are you?", false));
        messages.add(new Message("I'm fine, thank you!", true));
        messages.add(new Message("What about you?", true));
        messages.add(new Message("I'm good too", false));
        messages.add(new Message("That's great!", true));
        messages.add(new Message("Bye", false));
    }
}