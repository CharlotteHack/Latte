package com.salad.latte;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SuperInvestorRecentMovesActivity extends AppCompatActivity {

    TextView superInvestorName;
    TextView ytdPerformance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superinvestor_recentmoves);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Bundle bundle = getIntent().getExtras();
        superInvestorName = findViewById(R.id.tv_recents_investor);
        ytdPerformance = findViewById(R.id.tv_recents_ytd);
        superInvestorName.setText(bundle.getString("superInvestor"));
        ytdPerformance.setText(bundle.getString("ytd")+"% Return to date");


    }
}
