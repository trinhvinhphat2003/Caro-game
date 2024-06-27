package com.example.myapplication.socket;


import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    private static SocketManager instance;
    private Socket mSocket;
    private static final String SOCKET_URL = "http://10.0.2.2:8000";
    private SocketCallback callback;
    public SocketManager(String userId) {
        try {
            IO.Options options = new IO.Options();
            options.query = "userId=" + userId;
            mSocket = IO.socket(SOCKET_URL, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static synchronized SocketManager getInstance(String userId) {
        if (instance == null) {
            instance = new SocketManager(userId);
        }
        return instance;
    }
    public void connect() {
        mSocket.connect();

    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void disconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
        }
        instance = null;
    }
}
