package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, confirmEditText;
    private Button signupButton, toHome, toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toHome = findViewById(R.id.toHome);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmEditText = findViewById(R.id.confirm);
        signupButton = findViewById(R.id.signupButton);
        toLogin = findViewById(R.id.toLogin);

        toHome.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        toLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signupButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirm = confirmEditText.getText().toString();


            if (TextUtils.isEmpty(username)) {
                Toast.makeText(SignUpActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignUpActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(confirm)) {
                Toast.makeText(SignUpActivity.this, "Please enter your confirm password", Toast.LENGTH_SHORT).show();
            } else if (!confirm.equals(password)) {
                Toast.makeText(SignUpActivity.this, "Confirm password must be the same as password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUpActivity.this, "SIgn up successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
