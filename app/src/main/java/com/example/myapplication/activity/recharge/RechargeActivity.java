package com.example.myapplication.activity.recharge;

import android.os.Bundle;
import android.widget.ImageButton;

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

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recharge);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(16));

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Add recharge options
        rechargeOptions = new ArrayList<>();
        rechargeOptions.add(new RechargeOption("10,000 VND", R.drawable.coin_1, "100 coins"));
        rechargeOptions.add(new RechargeOption("20,000 VND", R.drawable.coin_2, "200 coins"));
        rechargeOptions.add(new RechargeOption("50,000 VND", R.drawable.coin_3, "500 coins"));
        rechargeOptions.add(new RechargeOption("100,000 VND", R.drawable.coin_4, "1000 coins"));
        rechargeOptions.add(new RechargeOption("200,000 VND", R.drawable.coin_5, "2000 coins"));
        rechargeOptions.add(new RechargeOption("500,000 VND", R.drawable.coin_6, "5000 coins"));

        adapter = new RechargeAdapter(rechargeOptions);
        recyclerView.setAdapter(adapter);
    }
}