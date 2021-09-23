package com.salad.latte;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.salad.latte.Objects.SuperInvestor.Holding;

import java.util.ArrayList;

public class SuperInvestorPositionsActivity extends AppCompatActivity {
    TextView investor;
    ListView postionsLV;
    ArrayList<Holding> holdings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superinvestor_positions);
        Bundle bundle = getIntent().getExtras();

        investor = findViewById(R.id.tv_superinvestor_positions_investor);
        investor.setText(bundle.getString("investor"));
        postionsLV = findViewById(R.id.lv_superinvestor_positions);

    }
}
