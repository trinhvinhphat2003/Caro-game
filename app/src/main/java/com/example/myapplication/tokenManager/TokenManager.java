package com.example.myapplication.tokenManager;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

public class TokenManager {
    private static String token = null;
    private static String id_user = null;
    private static JSONObject userObject = null;


    public static JSONObject getUserObject() {
        return userObject;
    }

    public static void setUserObject(JSONObject user) {
        TokenManager.userObject = user;
    }

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
