package com.salad.latte.Database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salad.latte.Adapters.DividendAdapter;
import com.salad.latte.Adapters.PieAdapter;
import com.salad.latte.Adapters.HistoricalAdapter;
import com.salad.latte.Adapters.RecentsAdapter;
import com.salad.latte.Adapters.SuperInvestorAdapter;
import com.salad.latte.Adapters.WatchListAdapter;
import com.salad.latte.Objects.Dividend;
import com.salad.latte.Objects.Pie;
import com.salad.latte.Objects.Historical;
import com.salad.latte.Objects.SuperInvestor.Holding;
import com.salad.latte.Objects.SuperInvestor.SIActivity;
import com.salad.latte.Objects.SuperInvestor.SuperInvestor;
import com.salad.latte.Objects.Watchlist;

import org.json.JSONException;
import org.json.JSONObject;

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
                //
                Collections.sort(watchlistItems);
                watchListAdapter = new WatchListAdapter(context,layout,watchlistItems);
                gridView.setAdapter(watchListAdapter);
                watchListAdapter.notifyDataSetChanged();
                dashboard_progress.setVisibility(View.INVISIBLE);
                int portfolioSize = 0;
                for (Watchlist item: watchlistItems
                     ) {
                    if(item.inOurPortfolio){
                        portfolioSize = portfolioSize + 1;
                    }
                }
                openPos.setText(portfolioSize+"");
                totalReturn.setText(getTotalReturn(watchlistItems)+"%");

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
