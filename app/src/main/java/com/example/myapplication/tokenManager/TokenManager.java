package com.example.myapplication.tokenManager;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static String token = null;
    private static String id_user = null;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        TokenManager.token = token;
    }
    public static void clearToken(){
        token = null;
        id_user = null;
    }

    public static String getId_user() {
        return id_user;
    }

    public static void setId_user(String id_user) {
        TokenManager.id_user = id_user;
    }
}
