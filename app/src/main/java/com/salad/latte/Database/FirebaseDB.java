package com.salad.latte.Database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salad.latte.Adapters.CustomDailyHistoricalAdapter;
import com.salad.latte.Adapters.DailyWatchlistAdapter;
import com.salad.latte.Adapters.DividendAdapter;
import com.salad.latte.Adapters.PieAdapter;
import com.salad.latte.Adapters.HistoricalAdapter;
import com.salad.latte.Adapters.RecentsAdapter;
import com.salad.latte.Adapters.SuperInvestorAdapter;
import com.salad.latte.Adapters.WatchListAdapter;
import com.salad.latte.Objects.DailyWatchlistHistoricalItem;
import com.salad.latte.Objects.DailyWatchlistItem;
import com.salad.latte.Objects.Dividend;
import com.salad.latte.Objects.Pie;
import com.salad.latte.Objects.Historical;
import com.salad.latte.Objects.SuperInvestor.Holding;
import com.salad.latte.Objects.SuperInvestor.SIActivity;
import com.salad.latte.Objects.SuperInvestor.SuperInvestor;
import com.salad.latte.Objects.Watchlist;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.salad.latte.GeneratePieData.generatePieData;

public class FirebaseDB {
    private final DatabaseReference mDatabase;
    ValueEventListener watchlistReference;
    ArrayList<Watchlist> watchlistItems;
    WatchListAdapter watchListAdapter;

    //
    ValueEventListener historicalReference;
    ArrayList<Historical> historicalItems;
    HistoricalAdapter historicalAdapter;

    ValueEventListener recentsReference;
    ArrayList<Watchlist> recentItems;
    RecentsAdapter recentsAdapter;

    ValueEventListener pieReference;
    ArrayList<Pie> pieItems;
    PieAdapter pieAdapter;

    ValueEventListener dividendsReference;
    ArrayList<Dividend> dividendItems;
    DividendAdapter dividendAdapters;

    ValueEventListener superInvestorReference;
    ArrayList<SuperInvestor> superInvestors;
    SuperInvestorAdapter superInvestorAdapter;

    ValueEventListener dailyPicksReference;
    public ArrayList<DailyWatchlistItem> dailyPicks;
    DailyWatchlistAdapter dailyWatchlistAdapter;

    ValueEventListener datesReference;

    ValueEventListener stockPricesReference;

    ValueEventListener dailyHistorialReference;
    public ArrayList<DailyWatchlistHistoricalItem> dailyHistoricalItems;
    CustomDailyHistoricalAdapter customDailyHistoricalAdapter;

    String updatedTime = "";
//

    double totalReturns = 0;

    int length = 0;
    public FirebaseDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        watchlistItems = new ArrayList<>();
        historicalItems = new ArrayList<>();
        //
        pieItems = new ArrayList<>();
        recentItems = new ArrayList<>();
        dividendItems = new ArrayList<>();
        superInvestors = new ArrayList<>();
        dailyPicks = new ArrayList<>();
        dailyHistoricalItems = new ArrayList<>();


    }

    public ArrayList<DailyWatchlistHistoricalItem> pullDailyHistoricalItems(Context context, RecyclerView recyclerView, TextView return_tv,ProgressBar historicalDailyPB){
        historicalDailyPB.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        if (dailyHistorialReference != null){
            mDatabase.removeEventListener(dailyHistorialReference);
        }
        customDailyHistoricalAdapter = new CustomDailyHistoricalAdapter(dailyHistoricalItems,context);
        dailyHistorialReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyHistoricalItems.clear();
                Float totalReturn = 0f;
                Float openPositionsTotalReturn = 0f;
                Float closedPostionsTotalReturn = 0f;
                int openPositionsCount = 0;
                int closedPositionsWins = 0;
                int closedPositionsLosses = 0;
                int closedPositionsCount = 0;
//                Log.d("FirebaseDB","Daily Snapshot count: "+snapshot.child("daily_picks").getChildrenCount());

                for(DataSnapshot datasnap: snapshot.child("daily_picks").getChildren()){
                    String keyDate = datasnap.getKey();
//                    Log.d("FirebaseDB","First date: "+keyDate);


                    for(DataSnapshot innerData : snapshot.child("daily_picks").child(keyDate).getChildren()){
                        String tick = innerData.getKey();
//                        Log.d("FirebaseDB","First ticker: "+tick);
                        Float exitPrice = innerData.child(tick).child("exitPrice").getValue(Float.class);
                        if(exitPrice > 0) {
                            dailyHistoricalItems.add(
                                    new DailyWatchlistHistoricalItem(
                                            innerData.child(tick).child("imgUrl").getValue(String.class),
                                            innerData.child(tick).child("ticker").getValue(String.class),
                                            keyDate,
                                            innerData.child(tick).child("entryPrice").getValue(Float.class),
                                            innerData.child(tick).child("exitPrice").getValue(Float.class),
                                            innerData.child(tick).child("allocation").getValue(Float.class)
                                    )
                            );
                            Float entry = innerData.child(tick).child("entryPrice").getValue(Float.class);
                            Float exit = innerData.child(tick).child("exitPrice").getValue(Float.class);
                            Float alloc = innerData.child(tick).child("allocation").getValue(Float.class);
                            float ret = ((exit - entry) / entry);
                            totalReturn = (totalReturn + ret);
                            closedPositionsCount = closedPositionsCount + 1;
                            closedPostionsTotalReturn = closedPostionsTotalReturn + ret;
                            if(ret < 0.0f){
                                closedPositionsLosses = closedPositionsLosses + 1;
                            }
                            else{
                                closedPositionsWins = closedPositionsWins + 1;
                            }

                        }
                        else if (exitPrice == 0f){
                            //These are open positions we have
                            Float entry = innerData.child(tick).child("entryPrice").getValue(Float.class);
                            Float currentPrice = innerData.child(tick).child("currentPrice").getValue(Float.class);
                            Float alloc = innerData.child(tick).child("allocation").getValue(Float.class)/100;
                            float ret = ((currentPrice - entry) / entry) ;
                            Log.d("OpenPosTracker: ","ATicker: "+tick+" Entry: "+entry);
                            Log.d("OpenPosTracker: ","ATicker: "+tick+" Current: "+currentPrice);
                            Log.d("OpenPosTracker: ","ATicker: "+tick+" Alloc: "+alloc);
//                            Log.d("OpenPosTracker: ","Ticker: "+tick+" Entry: "+String.format("%.02f", entry));

                            openPositionsTotalReturn = (openPositionsTotalReturn + (ret*alloc));
                            openPositionsCount = openPositionsCount + 1;
                        }
                    }
//                    Log.d("FirebaseDB", "Results found for dailyPicks: " + dailyPicks.size());


                }
                String formatTotalReturn = String.format("%.02f", totalReturn*100);
                String openPositionsRet = String.format("%.02f", openPositionsTotalReturn);
                Log.d("FirebaseDB","Return displayed: "+String.format("%.02f", totalReturn));
                Log.d("FirebaseDB","Return of closed positions: "+String.format("%.02f", closedPostionsTotalReturn));
                Log.d("FirebaseDB","Return of open positions "+String.format("%.02f", openPositionsTotalReturn));
                Log.d("FirebaseDB","Open Positions Count: "+openPositionsCount);
                Log.d("FirebaseDB","Closed Position Wins: "+closedPositionsWins);
                Log.d("FirebaseDB","Closed Position Losses: "+closedPositionsLosses);

                return_tv.setText("Total performance: "+formatTotalReturn+"%");
                Collections.reverse(dailyHistoricalItems);
                customDailyHistoricalAdapter.notifyDataSetChanged();
                Log.d("FirebaseDBSalad","Daily Historical items: "+dailyHistoricalItems.size());
                //
//                Log.d("FirebaseDB","Calling daily watchlist adapter");
                recyclerView.setAdapter(customDailyHistoricalAdapter);
                historicalDailyPB.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(dailyHistorialReference);
//        Log.d("DB Message","If this log was hit, sorry bro functions not working :(");
        return dailyHistoricalItems;
    }

    public void getCurrentStockPrice(DailyWatchlistItem dailyWatchlistItem,String ticker, DailyWatchlistAdapter adapter){
        if (stockPricesReference != null){
            mDatabase.removeEventListener(stockPricesReference);
        }
        try {
            stockPricesReference = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    float price = snapshot.child("stockPrices").child(ticker).child("price").getValue(Float.class);
                    dailyWatchlistItem.setCurrentPrice(price);
                    adapter.notifyDataSetChanged();

                    Log.d("FirebaseDB","dailyWatchlistItem ticker: "+ticker+" Current Price: "+price);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("FirebaseDB",e.getLocalizedMessage());

        }
        mDatabase.addListenerForSingleValueEvent(stockPricesReference);
    }
    public void setDailyDates(ArrayList<String> dailyDates, ArrayAdapter<String> spinnerAdapter, ProgressBar progressBar, FloatingActionButton floatingActionButton, RecyclerView recyclerView){
        progressBar.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        if (datesReference != null){
            mDatabase.removeEventListener(datesReference);
        }
        datesReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyDates.clear();
//                Log.d("FirebaseDB","Daily Snapshot count: "+snapshot.child("daily_picks").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("daily_picks").getChildren()){
                    String keyDate = datasnap.getKey();
//                    Log.d("FirebaseDB","Date found in daily_picks: "+keyDate);
                    dailyDates.add(keyDate);


                }
                Collections.reverse(dailyDates);
                spinnerAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
//                floatingActionButton.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(datesReference);
    }

    public void setMonthlyDates(ArrayList<String> dailyDates, ArrayAdapter<String> spinnerAdapter, ProgressBar progressBar, FloatingActionButton floatingActionButton, RecyclerView recyclerView){
        progressBar.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        if (datesReference != null){
            mDatabase.removeEventListener(datesReference);
        }
        datesReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyDates.clear();
//                Log.d("FirebaseDB","Daily Snapshot count: "+snapshot.child("daily_picks").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("daily_monthly_picks").getChildren()){
                    String keyDate = datasnap.getKey();
//                    Log.d("FirebaseDB","Date found in daily_picks: "+keyDate);
                    dailyDates.add(keyDate);


                }
                Collections.reverse(dailyDates);
                spinnerAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
//                floatingActionButton.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(datesReference);
    }

    public ArrayList<DailyWatchlistItem> pullDailyData(Context context, RecyclerView recyclerView){
//        recents_progress.setVisibility(View.VISIBLE);
        if (dailyPicksReference != null){
            mDatabase.removeEventListener(dailyPicksReference);
        }
        dailyWatchlistAdapter = new DailyWatchlistAdapter(dailyPicks,context);

        dailyPicksReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyPicks.clear();

//                Log.d("FirebaseDB","Daily Snapshot count: "+snapshot.child("daily_picks").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("daily_monthly_picks").getChildren()){
                    String keyDate = datasnap.getKey();
//                    Log.d("FirebaseDB","First date: "+keyDate);

                    for(DataSnapshot innerData : snapshot.child("daily_monthly_picks").child(keyDate).getChildren()){
                        String tick = innerData.getKey();
//                        Log.d("FirebaseDB","First ticker: "+tick);

                        dailyPicks.add(
                                new DailyWatchlistItem(
                                        innerData.child(tick).child("imgUrl").getValue(String.class),
                                        innerData.child(tick).child("ticker").getValue(String.class),
                                        innerData.child(tick).child("entryPrice").getValue(Float.class),
                                        innerData.child(tick).child("exitPrice").getValue(Float.class),
                                        innerData.child(tick).child("currentPrice").getValue(Float.class),
                                        innerData.child(tick).child("allocation").getValue(Float.class)
                                )
                        );
                    }
//                    Log.d("FirebaseDB", "Results found for dailyPicks: " + dailyPicks.size());
                    dailyWatchlistAdapter.notifyDataSetChanged();

                }
                //
//                Log.d("FirebaseDB","Calling daily watchlist adapter");
                recyclerView.setAdapter(dailyWatchlistAdapter);
//                recents_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(dailyPicksReference);
        return dailyPicks;
    }

    public ArrayList<DailyWatchlistItem> pullMonthDataForDate(Context context, RecyclerView recyclerView, String dateIn){
        if (dailyPicksReference != null){
            mDatabase.removeEventListener(dailyPicksReference);
        }
        dailyWatchlistAdapter = new DailyWatchlistAdapter(dailyPicks,context);
        dailyPicksReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyPicks.clear();

//                Log.d("FirebaseDB","Daily Snapshot count: "+snapshot.child("daily_picks").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("daily_monthly_picks").getChildren()) {
                    String keyDate = datasnap.getKey();
//                    Log.d("FirebaseDB","First date: "+keyDate);
                    if (keyDate.equals(dateIn)) {
                        for (DataSnapshot innerData : snapshot.child("daily_monthly_picks").child(keyDate).getChildren()) {
                            String tick = innerData.getKey();
//                        Log.d("FirebaseDB","First ticker: "+tick);
                            dailyPicks.add(
                                    new DailyWatchlistItem(
                                            innerData.child(tick).child("imgUrl").getValue(String.class),
                                            innerData.child(tick).child("ticker").getValue(String.class),
                                            innerData.child(tick).child("entryPrice").getValue(Float.class),
                                            innerData.child(tick).child("exitPrice").getValue(Float.class),
                                            innerData.child(tick).child("currentPrice").getValue(Float.class),
                                            innerData.child(tick).child("allocation").getValue(Float.class)
                                    )
                            );
                        }
                    }
//                    Log.d("FirebaseDB", "Results found for dailyPicks: " + dailyPicks.size());

                }
                //
//                Log.d("FirebaseDB","Calling daily watchlist adapter");
                recyclerView.setAdapter(dailyWatchlistAdapter);
                dailyWatchlistAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(dailyPicksReference);
        return dailyPicks;
    }

    public ArrayList<DailyWatchlistItem> pullDailyDataForDate(Context context, RecyclerView recyclerView, String dateIn){
        if (dailyPicksReference != null){
            mDatabase.removeEventListener(dailyPicksReference);
        }
        dailyWatchlistAdapter = new DailyWatchlistAdapter(dailyPicks,context);
        dailyPicksReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyPicks.clear();

//                Log.d("FirebaseDB","Daily Snapshot count: "+snapshot.child("daily_picks").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("daily_picks").getChildren()) {
                    String keyDate = datasnap.getKey();
//                    Log.d("FirebaseDB","First date: "+keyDate);
                    if (keyDate.equals(dateIn)) {
                        for (DataSnapshot innerData : snapshot.child("daily_picks").child(keyDate).getChildren()) {
                            String tick = innerData.getKey();
//                        Log.d("FirebaseDB","First ticker: "+tick);
                            dailyPicks.add(
                                    new DailyWatchlistItem(
                                            innerData.child(tick).child("imgUrl").getValue(String.class),
                                            innerData.child(tick).child("ticker").getValue(String.class),
                                            innerData.child(tick).child("entryPrice").getValue(Float.class),
                                            innerData.child(tick).child("exitPrice").getValue(Float.class),
                                            innerData.child(tick).child("currentPrice").getValue(Float.class),
                                            innerData.child(tick).child("allocation").getValue(Float.class)
                                    )
                            );
                        }
                }
//                    Log.d("FirebaseDB", "Results found for dailyPicks: " + dailyPicks.size());

                }
                //
//                Log.d("FirebaseDB","Calling daily watchlist adapter");
                recyclerView.setAdapter(dailyWatchlistAdapter);
                dailyWatchlistAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(dailyPicksReference);
        return dailyPicks;
    }

    public String pullUpdatedDailyPickTime(TextView updateTime){
        ValueEventListener returnTime = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot datasnap: snapshot.child("daily_picks_lastUpdate").getChildren()){
                    String key = datasnap.getKey();
                    Log.d("FirebaseDB","Updated Key: "+key);/////////
//                    Log.d("FirebaseDB","Updated Time: "+datasnap.getValue(String.class));
                    updateTime.setText("Stocks to buy on this date at or after market close. \nPrices updated as of: "+datasnap.getValue(String.class)+" EST");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(returnTime);

        return updatedTime;
    }
    public String pullUpdatedTime(TextView updateTime){
        ValueEventListener returnTime = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchlistItems.clear();

                for(DataSnapshot datasnap: snapshot.child("updateTime").getChildren()){
                    String key = datasnap.getKey();
//                    Log.d("FirebaseDB","Updated Key: "+key);/////////
//                    Log.d("FirebaseDB","Updated Time: "+datasnap.getValue(String.class));
                    updateTime.setText("Stock prices updated as of: "+datasnap.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(returnTime);

        return updatedTime;
    }
    public float getTotalReturn(ArrayList<Watchlist> watchlistItems){
        float totalReturn = 0.0f;
        for (Watchlist item: watchlistItems
             ) {
//            entry*x = current
            //current/entry
            if(item.inOurPortfolio) {
                float curr = Float.parseFloat(item.currentPrice);
                float entryP = Float.parseFloat(item.targetEntry);
                float alloc = Float.parseFloat(item.allocation.split(" ")[1].replace("%", ""));
                float margin = 2.0f;
                float ret = ((((curr / entryP) - 1) * margin) * alloc);
//                Log.d("FirebaseHelper:", "ticker " + item.ticker + " entry: " + item.targetEntry + " current: " +
//                        item.currentPrice + " return " + ret);

                totalReturn = totalReturn + ret;
            }
        }
        float f = totalReturn;
        String test = String.format("%.02f", f);
        return Float.parseFloat(test);
    }
    public ArrayList<Watchlist> pullWatchlistData(Context context, int layout, GridView gridView, ProgressBar dashboard_progress, TextView openPos, TextView totalReturn){
        dashboard_progress.setVisibility(View.VISIBLE);

        if (watchlistReference != null){
            mDatabase.removeEventListener(watchlistReference);
        }
       watchlistReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchlistItems.clear();

//                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("dashboard").child("watchlist").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("dashboard").child("watchlist").getChildren()){
                    String currentPrice = datasnap.child("currentPrice").getValue(String.class);
                    if(!currentPrice.equals("")) {
                        watchlistItems.add(
                                new Watchlist(
                                        datasnap.child("icon").getValue(String.class),
                                        datasnap.child("ticker").getValue(String.class),
                                        datasnap.child("targetEntry").getValue(String.class),
                                        datasnap.child("currentPrice").getValue(String.class),
                                        datasnap.child("allocation").getValue(String.class),
                                        datasnap.child("entryDate").getValue(String.class).split(" ")[0],
                                        datasnap.child("inOurPortfolio").getValue(Boolean.class)
                                )
                        );
                    }
                }
                if(watchlistItems.size() > 0) {
                    //
                    Log.d("FirebaseDB", "Size of watchlist: " + watchlistItems.size());
                    Collections.sort(watchlistItems);
                    watchListAdapter = new WatchListAdapter(context, layout, watchlistItems);
                    gridView.setAdapter(watchListAdapter);
                    watchListAdapter.notifyDataSetChanged();
                    dashboard_progress.setVisibility(View.INVISIBLE);
                    int portfolioSize = 0;
                    for (Watchlist item : watchlistItems
                    ) {
                        if (item.inOurPortfolio) {
                            portfolioSize = portfolioSize + 1;
                        }
                    }
                    openPos.setText(portfolioSize + "");
//                try {
                    totalReturn.setText(getTotalReturn(watchlistItems) + "%");
//                }
//                catch (NumberFormatException ex){
//                    Log.d("FirebaseDB","Error");
//                    ex.printStackTrace();
//                }
//                finally {
//                    totalReturn.setText("N/A" + "%");
//                }

                }
       }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(watchlistReference);
//        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return watchlistItems;
    }

    public ArrayList<Watchlist> pullRecentsData(Context context, int layout, ListView listView){
//        recents_progress.setVisibility(View.VISIBLE);
        if (recentsReference != null){
            mDatabase.removeEventListener(recentsReference);
        }
        recentsReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentItems.clear();

//                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("dashboard").child("watchlist").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("dashboard").child("watchlist").getChildren()){
                    recentItems.add(
                            new Watchlist(
                                    datasnap.child("icon").getValue(String.class),
                                    datasnap.child("ticker").getValue(String.class),
                                    datasnap.child("targetEntry").getValue(String.class),
                                    datasnap.child("currentPrice").getValue(String.class),
                                    datasnap.child("allocation").getValue(String.class),
                                    datasnap.child("entryDate").getValue(String.class),
                                    datasnap.child("inOurPortfolio").getValue(Boolean.class)
                            )
                    );
                }
                //
                Collections.reverse(recentItems);
                recentsAdapter = new RecentsAdapter(context,layout,recentItems);
                listView.setAdapter(recentsAdapter);
                recentsAdapter.notifyDataSetChanged();
//                recents_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(recentsReference);
        Log.d("FirebaseDB", "Results found for recents: " + recentItems.size());
        return recentItems;
    }


    public ArrayList<Watchlist> pullRecentsDataByDate(Context context, int layout, ListView listView, String month){
//        recents_progress.setVisibility(View.VISIBLE);
        if (recentsReference != null){
            mDatabase.removeEventListener(recentsReference);
        }
        recentsReference = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recentItems.clear();

//                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("dashboard").child("watchlist").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("dashboard").child("watchlist").getChildren()){
                    if(datasnap.child("entryDate").getValue(String.class).toLowerCase().contains(month.toLowerCase())) {
                        recentItems.add(
                                new Watchlist(
                                        datasnap.child("icon").getValue(String.class),
                                        datasnap.child("ticker").getValue(String.class),
                                        datasnap.child("targetEntry").getValue(String.class),
                                        datasnap.child("currentPrice").getValue(String.class),
                                        datasnap.child("allocation").getValue(String.class),
                                        datasnap.child("entryDate").getValue(String.class),
                                        datasnap.child("inOurPortfolio").getValue(Boolean.class)
                                )
                        );
                    }
                }
                //
                Collections.reverse(recentItems);
                recentsAdapter = new RecentsAdapter(context,layout,recentItems);
                listView.setAdapter(recentsAdapter);
                recentsAdapter.notifyDataSetChanged();
//                recents_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(recentsReference);
        Log.d("FirebaseDB", "Results found for recents: " + recentItems.size());
        return recentItems;
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

//                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
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
//        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());historical_progress.setVisibility(View.INVISIBLE);
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

//                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
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
//        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return historicalItems;
    }
    public ArrayList<Dividend> pullDividendsData(Context context, int layout, ListView listView){
        dividendItems.clear();
        Log.d("FirebaseDB","pullDividendData called");
        mDatabase.child("dividends").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase task", String.valueOf(task.getResult().getValue()));
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(task.getResult().getValue()));
//                        for(JSONObject json : jsonObject.)
                        Iterator<String> iter = jsonObject.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                JSONObject value = (JSONObject) jsonObject.get(key);
                                dividendItems.addAll(getDividendItems(value));
                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    dividendAdapters = new DividendAdapter(context,layout,dividendItems);
                    listView.setAdapter(dividendAdapters);
                    dividendAdapters.notifyDataSetChanged();
                    Log.d("FirebaseDB","Dividend size: "+dividendItems.size());


                }

            }
        });

        //Below is snapshot version
//        if (dividendsReference != null){
//            mDatabase.removeEventListener(dividendsReference);
//        }
//        dividendsReference = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                dividendItems.clear();
//
////                Log.d("FirebaseDB","Dividends Snapshot count: "+snapshot.child("dividends").getChildrenCount());
//                for(DataSnapshot datasnap: snapshot.child("dividends").getChildren()){
//                    String key = datasnap.getKey();
////                    Log.d("FirebaseDB","Dividend Key: "+key+" Children count "+snapshot.child("dividends").child(key).getChildrenCount());
//                    for(DataSnapshot innerSnap: snapshot.child("dividends").child(key).getChildren()) {
////                        Log.d("FirebaseDB: ","Dividend ticker: "+innerSnap.child("ticker").getValue(String.class));
//                    dividendItems.add(
//                            new Dividend(
//                                    datasnap.child("date").getValue(String.class),
//                                    datasnap.child("dateTimestamp").getValue(String.class),
//                                    datasnap.child("dividendAmount").getValue(String.class),
//                                    datasnap.child("ticker").getValue(String.class)
//                            )
//                    );
//                    }
//                }
//                dividendAdapters = new DividendAdapter(context,layout,dividendItems);
//                listView.setAdapter(recentsAdapter);
//                dividendAdapters.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        mDatabase.addValueEventListener(dividendsReference);
//        Log.d("FirebaseDB", "Results found for dividends: " + dividendItems.size());
//        Log.d("FirebaseDB","Dividend size: "+dividendItems.size());
        return dividendItems;
    }

    public ArrayList<Dividend> getDividendItems(JSONObject jsonObjects){
        ArrayList<Dividend> dividends = new ArrayList<>();
        try {
//                        for(JSONObject json : jsonObject.)
            Iterator<String> iter = jsonObjects.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                    JSONObject value = (JSONObject) jsonObjects.get(key);
//                    Log.d("firebase task 2", value.getString("date"));
//                    Log.d("firebase task 2", value.getString("ticker"));
//                    Log.d("firebase task 2", value.getString("dividendAmount"));
//                    Log.d("firebase task 2", value.getString("dateTimestamp"));
                    Dividend d = new Dividend(value.getString("date"),value.getString("dateTimestamp"),value.getString("dividendAmount"),value.getString("ticker"));
//                    Log.d("FirebaseDB","Added dividend: "+d.getTicker()+" "+d.getDate());
                    dividends.add(d);
            }
        }
        catch (Exception e){
            Log.d("firebase task 2", e.toString());
        }
        return dividends;
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

//                Log.d("FirebaseDB","Snapshot count: "+snapshot.child("pie").getChildrenCount());
                length =  Integer.parseInt(snapshot.child("pie").getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Log.d("FirebaseDB","Snapshot count return: "+length);
        return length;
    }


    public ArrayList<SuperInvestor> pullSuperInvestorData(Context context, int layout, ListView superInvestor_lv, ProgressBar superinvestor_progress){
        superinvestor_progress.setVisibility(View.VISIBLE);
        superInvestor_lv.setVisibility(View.INVISIBLE);
        if (superInvestorReference != null){
            mDatabase.removeEventListener(superInvestorReference);
        }
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                superInvestors.clear();

//              Log.d("FirebaseDB","Snapshot count: "+snapshot.child("historical").getChildrenCount());
                for(DataSnapshot datasnap: snapshot.child("superInvestors").getChildren()){
//                    Log.d("FirebaseDB dataSnap",datasnap.getKey()+" Children count: "+datasnap.getChildrenCount());
                    SuperInvestor superInvestor = new SuperInvestor("","","","","");
//                        for(DataSnapshot superI : snapshot.child("superInvestors").child(datasnap.getKey()).child("data").getChildren()){
//                            Log.d("FirebaseDB superI",datasnap.getKey()+" Children count: "+datasnap.getChildrenCount());

                            try {
//                                JSONObject jsonObject = new JSONObject(superI.getValue(String.class));
//                                Log.d("FirebaseDB Child",jsonObject.toString());
//                            for(DataSnapshot holdings : superI.getChildren()) {
                                if (snapshot.child("superInvestors").child(datasnap.getKey()).child("data").hasChild("generalInfo")) {
//                                    Log.d("FirebaseDB path",)
//                                    for (DataSnapshot path : snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("generalInfo")) {
                                    DataSnapshot path = snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("generalInfo");
                                    superInvestor.setAssetsUnderManagement(path.child("assestsUnderManagment").getValue(String.class));

                                    ;
                                    superInvestor.setCompanyName(path.child("companyName").getValue(String.class));
                                    ;
                                    superInvestor.setLinkToHoldings(path.child("linkToHoldings").getValue(String.class));
                                    ;
                                    superInvestor.setNumOfStocks(path.child("numOfStocks").getValue(String.class));
                                    ;
                                    superInvestor.setShortName(path.child("shortName").getValue(String.class));
//                                    Log.d("FirebaseDB","companyName from Firebase: "+path.child("shortName").getValue(String.class));
//                                    Log.d("FirebaseDB path key: ",superI.getKey());
//                                    Log.d("FirebaseDB","companyName: "+path.child("companyName"));

//                                    }
                                }

                                if (snapshot.child("superInvestors").child(datasnap.getKey()).child("data").hasChild("activities")) {
                                            for(DataSnapshot activitySnapshot : snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("activities").getChildren())
                                            {
                                                SIActivity siActivity = new SIActivity("","","","","","","");
                                                siActivity.setActivty_(activitySnapshot.child("activty_").getValue(String.class));
                                                siActivity.setChangeToPortfolio(activitySnapshot.child("changeToPortfolio").getValue(String.class));
                                                siActivity.setQtrYear(activitySnapshot.child("qtrYear").getValue(String.class));
                                                siActivity.setShareCountChange(activitySnapshot.child("shareCountChange").getValue(String.class));
                                                siActivity.setStock(activitySnapshot.child("stock").getValue(String.class));
                                                if(snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("activities").child(activitySnapshot.getKey()).hasChild("logo_url"))
                                                {
                                                    siActivity.setLogo_url(activitySnapshot.child("logo_url").getValue(String.class));

                                                }
                                                else{
                                                    siActivity.setLogo_url("..");
                                                }
                                                if(snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("activities").child(activitySnapshot.getKey()).hasChild("previousClose"))
                                                {
                                                    siActivity.setPreviousClose(activitySnapshot.child("previousClose").getValue(Double.class)+"");
                                                }
                                                else{
                                                    siActivity.setPreviousClose("0");
                                                }
                                                superInvestor.addActivity(siActivity);
//                                                Log.d("FirebaseDB qtrYear: ",activitySnapshot.child("qtrYear").getValue(String.class));

                                            }
                                    }



                                if (snapshot.child("superInvestors").child(datasnap.getKey()).child("data").hasChild("holdings")) {
                                    for(DataSnapshot holdingsSnapshot : snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("holdings").getChildren())
                                    {
                                        Holding holding = new Holding("","","","","","","","","");
                                        holding.setNumShares(holdingsSnapshot.child("numShares").getValue(String.class));
                                        holding.setPortfolioWeight(holdingsSnapshot.child("portfolioWeight").getValue(String.class));
                                        holding.setRecentActivity(holdingsSnapshot.child("recentActivity").getValue(String.class));
                                        holding.setReportedPrice(holdingsSnapshot.child("reportedPrice").getValue(String.class));
                                        holding.setStock(holdingsSnapshot.child("stock").getValue(String.class));
                                        holding.setValue(holdingsSnapshot.child("value").getValue(String.class));

                                        if(snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("holdings").child(holdingsSnapshot.getKey()).hasChild("logo_url"))
                                        {
                                            holding.setLogo_url(holdingsSnapshot.child("logo_url").getValue(String.class));

                                        }
                                        else{
                                            holding.setLogo_url("..");
                                        }
                                        if(snapshot.child("superInvestors").child(datasnap.getKey()).child("data").child("holdings").child(holdingsSnapshot.getKey()).hasChild("previousClose"))
                                        {
                                            holding.setPreviousClose(holdingsSnapshot.child("previousClose").getValue(Double.class)+"");
                                        }
                                        else{
                                            holding.setPreviousClose("0");
                                        }
                                        holding.setTotalReturn();
                                        superInvestor.addHolding(holding);
//                                        Log.d("FirebaseDB qtrYear: ",activitySnapshot.child("qtrYear").getValue(String.class));

                                    }
                                }

                                superInvestor.calculateReturn();
                            }
                            catch (Exception e){
                                Log.d("FirebaseDB", e.getMessage());
                                e.printStackTrace();
                            }
//                            }
//                        int i = 0;
//                        //i represents each child in dividends .. like the divDate, divClosingPrice etc.
//                        //for(DataSnapshot innerArray :dividend.getChildren()){
//                        ArrayList<String> innerArray = new ArrayList<>();
//                        innerArray.add(
//                                dividend.child("0").getValue(String.class));
//                        innerArray.add(
//                                dividend.child("1").getValue(String.class));
//                        innerArray.add(
//                                dividend.child("2").getValue(String.class));
//                        innerArray.add(
//                                dividend.child("3").getValue(String.class));
//                        listOfDividends.add(innerArray);
                        //}
                        //
                    superInvestors.add(superInvestor);
                }
                //
                Collections.sort(superInvestors);
                superInvestorAdapter = new SuperInvestorAdapter(context,layout,superInvestors);
                superInvestor_lv.setAdapter(superInvestorAdapter);
                superInvestorAdapter.notifyDataSetChanged();
                superinvestor_progress.setVisibility(View.INVISIBLE);
                superInvestor_lv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Log.d("FirebaseDB", "Results found for watchlist: " + watchlistItems.size());
        return superInvestors;
    }

    public static float getCurrentPriceForStock(String ticker){
            return 0.0f;
    }
    public static float getEntryPriceForStock(String ticker){
        return 0.0f;
    }
}
