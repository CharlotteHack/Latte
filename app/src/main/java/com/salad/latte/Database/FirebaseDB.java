package com.salad.latte.Database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


    double totalReturns = 0;

    int length = 0;
    public FirebaseDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        watchlistItems = new ArrayList<>();
        historicalItems = new ArrayList<>();
        pieItems = new ArrayList<>();

    }

    public ArrayList<Watchlist> pullWatchlistData(Context context, int layout, GridView gridView, ProgressBar dashboard_progress){
        dashboard_progress.setVisibility(View.VISIBLE);

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
                Collections.sort(watchlistItems);
                watchListAdapter = new WatchListAdapter(context,layout,watchlistItems);
                gridView.setAdapter(watchListAdapter);
                watchListAdapter.notifyDataSetChanged();
                dashboard_progress.setVisibility(View.INVISIBLE);
       }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(watchlistReference);
        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return watchlistItems;
    }
    public ArrayList<Historical> pullHistoricalData(Context context, int layout, ListView historicalList, ProgressBar historical_progress){
        historical_progress.setVisibility(View.VISIBLE);
        if (historicalReference != null){
            mDatabase.removeEventListener(historicalReference);
        }
        historicalReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> youNameArray = new ArrayList<>();
                historicalItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("historical").getChildren()){
                    ArrayList<ArrayList<String>> listOfDividends = new ArrayList<ArrayList<String>>();
                    Historical historicalItem = new Historical("","","","","","","","","",listOfDividends);
                    for(DataSnapshot dividend: datasnap.child("dividends").getChildren()){
                        int i = 0;
                        //i represents each child in dividends .. like the divDate, divClosingPrice etc.
                        //for(DataSnapshot innerArray :dividend.getChildren()){
                        ArrayList<String> innerArray = new ArrayList<>();
                        innerArray.add(
                                dividend.child("0").getValue(String.class));
                        innerArray.add(
                                dividend.child("1").getValue(String.class));
                        innerArray.add(
                                dividend.child("2").getValue(String.class));
                        innerArray.add(
                                dividend.child("3").getValue(String.class));
                            listOfDividends.add(innerArray);
                        //}
                        //
                    }

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
                                    datasnap.child("allocation").getValue(String.class)+"",
                                    listOfDividends

                    ));
                }
                //
                historicalAdapter = new HistoricalAdapter(context,layout,historicalItems);
                historicalList.setAdapter(historicalAdapter);
                historicalAdapter.notifyDataSetChanged();

                historical_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(historicalReference);
        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return historicalItems;
    }
    public ArrayList<Historical> pullHistoricalDataByDate(Context context, int layout, ListView historicalList, String exitDate, ProgressBar historical_progress){
        totalReturns = 0;
        historical_progress.setVisibility(View.VISIBLE);
        //
        if (historicalReference != null){
            mDatabase.removeEventListener(historicalReference);
        }

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historicalItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("historical").getChildren()){
                    ArrayList<ArrayList<String>> listOfDividends = new ArrayList<ArrayList<String>>();
                    Historical historicalItem = new Historical("","","","","","","","","",listOfDividends);
                    for(DataSnapshot dividend: datasnap.child("dividends").getChildren()){
                        int i = 0;
                        //i represents each child in dividends .. like the divDate, divClosingPrice etc.
//                        for(DataSnapshot innerArray :dividend.getChildren()){
                        //i represents each child in dividends .. like the divDate, divClosingPrice etc.
                        //for(DataSnapshot innerArray :dividend.getChildren()){
                        ArrayList<String> innerArray = new ArrayList<>();
                        innerArray.add(
                                dividend.child("0").getValue(String.class));
                        innerArray.add(
                                dividend.child("1").getValue(String.class));
                        innerArray.add(
                                dividend.child("2").getValue(String.class));
                        innerArray.add(
                                dividend.child("3").getValue(String.class));
                        listOfDividends.add(innerArray);
                        //}
//                        }
                    }
                    if (Integer.parseInt(exitDate) == Integer.parseInt(datasnap.child("period").getValue(String.class).split(" ")[2].split("-")[0])) {
                        historicalItems.add(
                                new Historical(
                                        datasnap.child("ticker").getValue(String.class) + "",
                                        datasnap.child("period").getValue(String.class) + "",
                                        "",
                                        "",
                                        "",
                                        datasnap.child("entryPrice").getValue(String.class) + "",
                                        datasnap.child("exitPrice").getValue(String.class) + "",
                                        "",
                                        datasnap.child("allocation").getValue(String.class) + "",
                                        listOfDividends
                                )
                        );
                        totalReturns = totalReturns + historicalItems.get(historicalItems.size()-1).getReturnPercent();
                    }
                }

                Log.d("FirebaseDB","Year: "+exitDate+" Returns: "+totalReturns);
                //
                historicalAdapter = new HistoricalAdapter(context,layout,historicalItems);
                historicalList.setAdapter(historicalAdapter);
                historicalAdapter.notifyDataSetChanged();

                historical_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.addValueEventListener(historicalReference);
        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return historicalItems;
    }

    public ArrayList<Historical> pullHistoricalDataByDateOnce(Context context, int layout, ListView historicalList, String exitDate, ProgressBar historical_progress){
        totalReturns = 0;
        historical_progress.setVisibility(View.VISIBLE);
        //
        if (historicalReference != null){
            mDatabase.removeEventListener(historicalReference);
        }

        historicalReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historicalItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("historical").getChildren()){
                    ArrayList<ArrayList<String>> listOfDividends = new ArrayList<ArrayList<String>>();
                    Historical historicalItem = new Historical("","","","","","","","","",listOfDividends);
                    for(DataSnapshot dividend: datasnap.child("dividends").getChildren()){
                        int i = 0;
                        //i represents each child in dividends .. like the divDate, divClosingPrice etc.
//                        for(DataSnapshot innerArray :dividend.getChildren()){
                            //i represents each child in dividends .. like the divDate, divClosingPrice etc.
                            //for(DataSnapshot innerArray :dividend.getChildren()){
                            ArrayList<String> innerArray = new ArrayList<>();
                            innerArray.add(
                                    dividend.child("0").getValue(String.class));
                            innerArray.add(
                                    dividend.child("1").getValue(String.class));
                            innerArray.add(
                                    dividend.child("2").getValue(String.class));
                            innerArray.add(
                                    dividend.child("3").getValue(String.class));
                            listOfDividends.add(innerArray);
                            //}
//                        }
                    }
                    if (Integer.parseInt(exitDate) == Integer.parseInt(datasnap.child("period").getValue(String.class).split(" ")[2].split("-")[0])) {
                        historicalItems.add(
                                new Historical(
                                        datasnap.child("ticker").getValue(String.class) + "",
                                        datasnap.child("period").getValue(String.class) + "",
                                        "",
                                        "",
                                        "",
                                        datasnap.child("entryPrice").getValue(String.class) + "",
                                        datasnap.child("exitPrice").getValue(String.class) + "",
                                        "",
                                        datasnap.child("allocation").getValue(String.class) + "",
                                        listOfDividends
                                )
                        );
                        totalReturns = totalReturns + historicalItems.get(historicalItems.size()-1).getReturnPercent();
                    }
                }

                Log.d("FirebaseDB","Year: "+exitDate+" Returns: "+totalReturns);
                //
                historicalAdapter = new HistoricalAdapter(context,layout,historicalItems);
                historicalList.setAdapter(historicalAdapter);
                historicalAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());historical_progress.setVisibility(View.INVISIBLE);
        Log.d("FirebaseDB","Clicked Historical Data for specific date");
        return historicalItems;
    }


    public ArrayList<Historical> pullHistoricalDataOnce(Context context, int layout, ListView historicalList, ProgressBar historical_progress){
        historical_progress.setVisibility(View.VISIBLE);
        if (historicalReference != null){
            mDatabase.removeEventListener(historicalReference);
        }
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> youNameArray = new ArrayList<>();
                historicalItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("historical").getChildren()){
                    ArrayList<ArrayList<String>> listOfDividends = new ArrayList<ArrayList<String>>();
                    Historical historicalItem = new Historical("","","","","","","","","",listOfDividends);
                    for(DataSnapshot dividend: datasnap.child("dividends").getChildren()){
                        int i = 0;
                        //i represents each child in dividends .. like the divDate, divClosingPrice etc.
                        //for(DataSnapshot innerArray :dividend.getChildren()){
                        ArrayList<String> innerArray = new ArrayList<>();
                        innerArray.add(
                                dividend.child("0").getValue(String.class));
                        innerArray.add(
                                dividend.child("1").getValue(String.class));
                        innerArray.add(
                                dividend.child("2").getValue(String.class));
                        innerArray.add(
                                dividend.child("3").getValue(String.class));
                        listOfDividends.add(innerArray);
                        //}
                        //
                    }

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
                                    datasnap.child("allocation").getValue(String.class)+"",
                                    listOfDividends

                            ));
                }
                //
                historicalAdapter = new HistoricalAdapter(context,layout,historicalItems);
                historicalList.setAdapter(historicalAdapter);
                historicalAdapter.notifyDataSetChanged();
                historical_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return historicalItems;
    }

    public int getPieCount(){
        length = 0;
        if (pieReference != null){
            mDatabase.removeEventListener(pieReference);
        }
        pieReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pieItems.clear();

                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("pie").getChildrenCount());
                length =  Integer.parseInt(snapshot.child("pie").getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Log.d("FirebaseDB","Snapshot count return: "+length);
        return length;
    }



}
