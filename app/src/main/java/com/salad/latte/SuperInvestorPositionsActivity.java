package com.salad.latte;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.salad.latte.Adapters.SuperInvestorPositionsAdapter;
import com.salad.latte.Objects.SuperInvestor.Holding;

import java.util.ArrayList;

public class SuperInvestorPositionsActivity extends AppCompatActivity {
    TextView investor;
    ListView postionsLV;
    ArrayList<Holding> holdings;
    View finish;
    SuperInvestorPositionsAdapter superInvestorPositionsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superinvestor_positions);
        Bundle bundle = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        investor = findViewById(R.id.tv_superinvestor_positions_investor);
        investor.setText(bundle.getString("investor"));
        postionsLV = findViewById(R.id.lv_superinvestor_positions);
        finish = findViewById(R.id.btn_positions_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        holdings = (ArrayList<Holding>) getIntent().getSerializableExtra("holdings");
        superInvestorPositionsAdapter = new SuperInvestorPositionsAdapter(this,R.layout.custom_superinvestor_positions,holdings);
        postionsLV.setAdapter(superInvestorPositionsAdapter);
        superInvestorPositionsAdapter.notifyDataSetChanged();
        Log.d("SuperInvestorPosAct","Len: "+holdings.size());


    }
}
