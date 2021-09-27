package com.salad.latte;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.salad.latte.Adapters.RecentsAdapter;
import com.salad.latte.Database.FirebaseDB;
import com.salad.latte.Objects.Watchlist;

import java.util.ArrayList;

public class RecentMovesActivity extends AppCompatActivity {


    ListView recentsList;
    ArrayList<Watchlist> recentItems;
    RecentsAdapter recentsAdapter;
    FirebaseDB firebaseDB;
    Spinner recents_spinner;
    Context context;
    View recentMovesFinish;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_recentmoves);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        context = this;
        firebaseDB = new FirebaseDB();
        recentItems = new ArrayList();
        recentsList = findViewById(R.id.list_recents);
        recents_spinner = findViewById(R.id.spinner_recents);
        recentMovesFinish = findViewById(R.id.btn_ourrecentmoves_finish);
        recentItems.addAll(firebaseDB.pullRecentsData(this,R.layout.custom_recents,recentsList));
        Log.d("RecentMovesActivity", "Results found for Recents: " + recentItems.size());
        recentsAdapter = new RecentsAdapter(this,R.layout.custom_recents,recentItems);

        recentMovesFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] languages = getResources().getStringArray(R.array.spinner_recents);
        if (recents_spinner != null) {
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, languages);
            recents_spinner.setAdapter(adapter);
        }
        recents_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(languages[position].contains("All")){

                    recentItems.clear();
                    recentItems.addAll(firebaseDB.pullRecentsData(context,R.layout.custom_recents,recentsList));
                    recentsAdapter.notifyDataSetChanged();
                }
                else{
                    String month = languages[position].split(" ")[0];

                    recentItems.clear();
                    recentItems = firebaseDB.pullRecentsDataByDate(context,R.layout.custom_historical,recentsList,month);
                    recentsAdapter.notifyDataSetChanged();
                    Log.d("HistoricalFragment: ","Year to search for: "+month);
                    //Log.d("HistoricalFragment: ","# Historical Items: "+historicalItems.size)
                    //Log.d("HistoricalFragment: ","Num exits in the year: "+languages[position]+" : "+historicalItems.size)
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
