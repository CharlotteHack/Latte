package com.salad.latte.Database;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salad.latte.Adapters.PieAdapter;
import com.salad.latte.Adapters.HistoricalAdapter;
import com.salad.latte.Adapters.WatchListAdapter;
import com.salad.latte.Objects.Pie;
import com.salad.latte.Objects.Historical;
import com.salad.latte.Objects.Watchlist;

import java.util.ArrayList;

import static com.salad.latte.GeneratePieData.generatePieData;

public class FirebaseDB {
    private final DatabaseReference mDatabase;
    ValueEventListener watchlistReference;
    ArrayList<Watchlist> watchlistItems;
    WatchListAdapter watchListAdapter;

    ValueEventListener historicalReference;
    ArrayList<Historical> historicalItems;
    HistoricalAdapter historicalAdapter;

    ValueEventListener pieReference;
    ArrayList<Pie> pieItems;
    PieAdapter pieAdapter;
    public FirebaseDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        watchlistItems = new ArrayList<>();
        historicalItems = new ArrayList<>();
        pieItems = new ArrayList<>();

    }

    public ArrayList<Watchlist> pullWatchlistData(Context context, int layout, GridView gridView){

        if (watchlistReference != null){
            mDatabase.removeEventListener(watchlistReference);
        }
       watchlistReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchlistItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("dashboard").child("watchlist").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("dashboard").child("watchlist").getChildren()){
                    watchlistItems.add(
                            new Watchlist(
                                    datasnap.child("icon").getValue(String.class),
                                    datasnap.child("ticker").getValue(String.class),
                                    datasnap.child("targetEntry").getValue(String.class),
                                    datasnap.child("currentPrice").getValue(String.class),
                                    datasnap.child("allocation").getValue(String.class),
                                    datasnap.child("entryDate").getValue(String.class)
                            )
                    );
                }
                //
                watchListAdapter = new WatchListAdapter(context,layout,watchlistItems);
                gridView.setAdapter(watchListAdapter);
                watchListAdapter.notifyDataSetChanged();
       }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(watchlistReference);
        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return watchlistItems;
    }
    public ArrayList<Historical> pullHistoricalData(Context context, int layout, ListView historicalList){

        if (historicalReference != null){
            mDatabase.removeEventListener(historicalReference);
        }
        historicalReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historicalItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("historical").getChildren()){
                    historicalItems.add(
                            new Historical(
                                    datasnap.child("ticker").getValue(String.class)+"",
                                    datasnap.child("period").getValue(String.class)+"",
                                    "",
                                    "",
                                    "",
                                    datasnap.child("entryPrice").getValue(String.class)+"",
                                    datasnap.child("exitPrice").getValue(String.class)+"",
                                    "",
                                    datasnap.child("allocation").getValue(String.class)+""
                            )
                    );
                }
                //
                historicalAdapter = new HistoricalAdapter(context,layout,historicalItems);
                historicalList.setAdapter(historicalAdapter);
                historicalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(historicalReference);
        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return historicalItems;
    }

    public ArrayList<Pie> pullPieChart(Context context, int layout, ListView closedList, PieChart pieChart){

        if (pieReference != null){
            mDatabase.removeEventListener(pieReference);
        }
        pieReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pieItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("pie").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("pie").getChildren()){
                    pieItems.add(
                            new Pie(
                                    datasnap.child("icon").getValue(String.class)+"",
                                    datasnap.child("ticker").getValue(String.class)+"",
                                    datasnap.child("entryDate").getValue(String.class)+"",
                                    datasnap.child("entryPrice").getValue(String.class)+"",
                                    datasnap.child("currentPrice").getValue(String.class)+"",
                                    datasnap.child("allocation").getValue(String.class)+""
                            )
                    );
                }
                //
                pieAdapter = new PieAdapter(context,layout,pieItems);
                closedList.setAdapter(pieAdapter);
                pieAdapter.notifyDataSetChanged();
                pieChart.setData(generatePieData(context,pieItems.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(pieReference);
        Log.d("FirebaseDB", "Results found for Pie: " + pieItems.size());
        return pieItems;
    }
}
