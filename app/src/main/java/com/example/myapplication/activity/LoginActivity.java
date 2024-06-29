package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.AuthRespository;
import com.example.myapplication.model.request.LoginRequest;
import com.example.myapplication.model.response.LoginResponse;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.tokenManager.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton, toHome, toSignup;
    AuthService authService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toHome = findViewById(R.id.toHome);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        toSignup = findViewById(R.id.toSignup);

        authService = AuthRespository.getAuthService();


        toHome.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        toSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            handleLogin();
        });
    }

    private void handleLogin(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            LoginRequest loginRequest = new LoginRequest(username, password);
            try {
                Call<LoginResponse> call = authService.login(loginRequest);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.body() != null){
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isOnSuccess()) {
                                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                TokenManager.setToken(loginResponse.getData().getToken());
                                TokenManager.setId_user(loginResponse.getData().get_id());
                                JSONObject userObject = new JSONObject();
                                try {
                                    userObject.put("_id", loginResponse.getData().get_id());
                                    userObject.put("fullName", loginResponse.getData().getFullName());
                                    userObject.put("profilePic", loginResponse.getData().getProfilePic());
                                    userObject.put("token", loginResponse.getData().getToken());
                                    userObject.put("wallet", loginResponse.getData().getWallet());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                TokenManager.setUserObject(userObject);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failedd", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
