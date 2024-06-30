package com.example.myapplication.activity.recharge;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.RechargeAdapter;
import com.example.myapplication.model.RechargeOption;

import java.util.ArrayList;
import java.util.List;

public class RechargeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RechargeAdapter adapter;
    private List<RechargeOption> rechargeOptions;
    private WebView webView;
    private ImageButton backButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recharge);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recharge), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        recyclerView = findViewById(R.id.recyclerView);
        webView = findViewById(R.id.webView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,   2));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(16));

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Add recharge options
        rechargeOptions = new ArrayList<>();
        rechargeOptions.add(new RechargeOption(10000, R.drawable.coin_1, 100));
        rechargeOptions.add(new RechargeOption(20000, R.drawable.coin_2, 220));
        rechargeOptions.add(new RechargeOption(50000, R.drawable.coin_3, 580));
        rechargeOptions.add(new RechargeOption(100000, R.drawable.coin_4, 1250));
        rechargeOptions.add(new RechargeOption(200000, R.drawable.coin_5, 2500));
        rechargeOptions.add(new RechargeOption(500000, R.drawable.coin_6, 8000));

        adapter = new RechargeAdapter(this, rechargeOptions, webView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            webView.goBack();
        } else if (webView.getVisibility() == View.VISIBLE) {
            webView.setVisibility(View.GONE); // Hide WebView if it's visible but cannot go back
            Toast.makeText(this, "Cancel transaction", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}