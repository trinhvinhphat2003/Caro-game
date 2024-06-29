package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.AuthRespository;
import com.example.myapplication.model.request.SignUpRequest;
import com.example.myapplication.model.response.SignUpResponse;
import com.example.myapplication.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, confirmEditText, fullnameEditText;
    private Button signupButton, toHome, toLogin;
    private RadioGroup genderRadioGroup;
    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toHome = findViewById(R.id.toHome);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmEditText = findViewById(R.id.confirm);
        fullnameEditText = findViewById(R.id.fullname);
        genderRadioGroup = findViewById(R.id.gender_group);
        signupButton = findViewById(R.id.signupButton);
        toLogin = findViewById(R.id.toLogin);
        authService = AuthRespository.getAuthService();

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
            }else if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(SignUpActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(fullnameEditText.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            } else {
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String gender = radioButton.getText().toString();
                SignUpRequest signUpRequest = new SignUpRequest(fullnameEditText.getText().toString(), username, password, confirm, gender);
                try {
                    Call<SignUpResponse> call = authService.signup(signUpRequest);
                    call.enqueue(new Callback<SignUpResponse>() {
                        @Override
                        public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                            if (response.body() != null){
                                SignUpResponse signUpResponse = response.body();
                                if (signUpResponse.isOnSuccess()) {
                                    Toast.makeText(SignUpActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SignUpResponse> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
