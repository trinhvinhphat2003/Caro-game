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
        rechargeOptions.add(new RechargeOption("10k", R.drawable.baseline_home_24, "100 Coins"));
        rechargeOptions.add(new RechargeOption("20k", R.drawable.baseline_home_24, "200 Coins"));
        rechargeOptions.add(new RechargeOption("50k", R.drawable.baseline_home_24, "500 Coins"));
        rechargeOptions.add(new RechargeOption("100k", R.drawable.baseline_home_24, "1000 Coins"));
        rechargeOptions.add(new RechargeOption("200k", R.drawable.baseline_home_24, "2000 Coins"));
        rechargeOptions.add(new RechargeOption("500k", R.drawable.baseline_home_24, "5000 Coins"));
        rechargeOptions.add(new RechargeOption("1tr", R.drawable.baseline_home_24, "10000 Coins"));
        rechargeOptions.add(new RechargeOption("2tr", R.drawable.baseline_home_24, "20000 Coins"));

        adapter = new RechargeAdapter(rechargeOptions);
        recyclerView.setAdapter(adapter);
    }
}