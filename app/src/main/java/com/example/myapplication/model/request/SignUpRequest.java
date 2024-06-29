package com.example.myapplication.model.request;

public class SignUpRequest {
    private String fullName;
    private String username;
    private String password;
    private String confirmPassword;
    private String gender;

    public SignUpRequest(String fullName, String username, String password, String confirmPassword, String gender) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.gender = gender;
    }
}
