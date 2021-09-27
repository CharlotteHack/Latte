package com.salad.latte;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.salad.latte.Adapters.DividendAdapter;
import com.salad.latte.Database.FirebaseDB;
import com.salad.latte.Objects.Dividend;

import java.util.ArrayList;

public class DividendsActivity extends AppCompatActivity {


    ListView lv_dividends;
    ArrayList<Dividend> dividendsList;
    DividendAdapter dividendAdapter;
    FirebaseDB firebaseDB;
    View finishButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_dividends);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        dividendsList = new ArrayList();
        firebaseDB = new FirebaseDB();;
        finishButton = findViewById(R.id.btn_ourdividends_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_dividends = findViewById(R.id.lv_dividends);
//        dividendsList.add(Dividend("-","2","2","3"))
        dividendsList.addAll(firebaseDB.pullDividendsData(this,R.layout.custom_dividends,lv_dividends));
    }
}
