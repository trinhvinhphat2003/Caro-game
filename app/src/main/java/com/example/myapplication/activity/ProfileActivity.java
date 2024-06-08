package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class ProfileActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private ImageButton editUsernameButton;
    private ImageButton editEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Finish the activity to go back
            }
        });

        // Khai báo các thành phần trên giao diện
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        editUsernameButton = findViewById(R.id.editUsername);
        editEmailButton = findViewById(R.id.editEmail);

        // Thiết lập sự kiện khi nhấn nút chỉnh sửa cho Username
        editUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở khóa EditText để chỉnh sửa
                usernameEditText.setFocusableInTouchMode(true);
                usernameEditText.setClickable(true);
                usernameEditText.setFocusable(true);
                usernameEditText.requestFocus();
            }
        });

        // Thiết lập sự kiện khi nhấn nút chỉnh sửa cho Email
        editEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở khóa EditText để chỉnh sửa
                emailEditText.setFocusableInTouchMode(true);
                emailEditText.setClickable(true);
                emailEditText.setFocusable(true);
                emailEditText.requestFocus();
            }
        });

        // Load user data (you can load it from your database or shared preferences)
        loadUserData();

        // Xử lý sự kiện khi nhấn nút ghi
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lưu thông tin sau khi chỉnh sửa
                saveUserInfo();
                // Khóa EditText để ngăn người dùng chỉnh sửa tiếp
                lockEditText();
            }
        });
    }

    private void loadUserData() {
        // For demonstration, hardcoding the values. Replace it with actual user data.
        usernameEditText.setText("John Doe");
        emailEditText.setText("john.doe@example.com");
    }

    // Hàm lưu thông tin người dùng
    private void saveUserInfo() {
        String newUsername = usernameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        // Thực hiện lưu thông tin vào cơ sở dữ liệu hoặc nơi lưu trữ khác
        // Ví dụ: SharedPreferences, Room Database, API call, ...
    }

    // Hàm khóa EditText để ngăn người dùng chỉnh sửa
    private void lockEditText() {
        usernameEditText.setFocusableInTouchMode(false);
        usernameEditText.setClickable(false);
        usernameEditText.setFocusable(false);
        emailEditText.setFocusableInTouchMode(false);
        emailEditText.setClickable(false);
        emailEditText.setFocusable(false);
    }
}