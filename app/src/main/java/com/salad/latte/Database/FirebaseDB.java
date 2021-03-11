package com.salad.latte.Database;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salad.latte.Adapters.WatchListAdapter;
import com.salad.latte.Objects.Watchlist;
import com.salad.latte.R;

import java.io.Console;
import java.util.ArrayList;

public class FirebaseDB {
    private final DatabaseReference mDatabase;
    ValueEventListener watchlistReference;
    ArrayList<Watchlist> watchlistItems;
    WatchListAdapter watchListAdapter;
    public FirebaseDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        watchlistItems = new ArrayList();

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
                                    datasnap.child("allocation").getValue(String.class),
                                    datasnap.child("buySellHold").getValue(String.class),
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
}
