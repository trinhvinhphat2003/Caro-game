package com.example.myapplication.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.fragment.home.HomeFragment;
import com.example.myapplication.fragment.message.MessageFragment;
import com.example.myapplication.socket.SocketManager;
import com.example.myapplication.tokenManager.TokenManager;

public class MainActivity extends AppCompatActivity {
//    Button toLogin;
    ActivityMainBinding binding;
    private SocketManager socketManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        socketManager = new SocketManager(TokenManager.getId_user());
        socketManager.connect();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId =  item.getItemId();
            if(itemId == R.id.homeTab) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.messageTab) {
                replaceFragment(new MessageFragment());
            }
            return true;
        });

//        toLogin = findViewById(R.id.toLogin);
//        toLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.disconnect();
        }
    }


}