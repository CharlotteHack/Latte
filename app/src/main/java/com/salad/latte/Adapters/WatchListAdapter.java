package com.salad.latte.Adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.salad.latte.MainActivity;
import com.salad.latte.Objects.News;
import com.salad.latte.Objects.Watchlist;
import com.salad.latte.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WatchListAdapter extends ArrayAdapter<Watchlist> {

    ArrayList<Watchlist> watchitems;
    MainActivity rootContext;
    ListView watchlist;

    ListView lv_news;
    ArrayList<News> news;
    NewsAdapter newsAdapter;

    public WatchListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Watchlist> objects) {
        super(context, resource, objects);
        watchitems = objects;
        rootContext = (MainActivity) context;
    }

    @Override
    public int getCount() {
        return watchitems.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = ((LayoutInflater)rootContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_watchlist_item,parent,false);
        if(!watchitems.get(position).icon.equals("")){
            //((FloatingActionButton) view.findViewById(R.id.floatingActionButton))..setImageURI(watchitems.get(position).icon);
            Picasso.get().load(watchitems.get(position).icon).into(((FloatingActionButton) view.findViewById(R.id.floatingActionButton)));

        }
        ((TextView) view.findViewById(R.id.tv_watchlist_ticker)).setText(watchitems.get(position).ticker);
        ((TextView) view.findViewById(R.id.tv_targetEntry)).setText("Entry Price: "+watchitems.get(position).targetEntry);
        ((TextView) view.findViewById(R.id.tv_allocation)).setText(watchitems.get(position).allocation);
        ((TextView) view.findViewById(R.id.tv_watchlist_entryDate)).setText(watchitems.get(position).entryDate);
        ((TextView) view.findViewById(R.id.tv_watchlist_currentPrice)).setText("Current Price: "+watchitems.get(position).currentPrice);

        float delta = ((Float.parseFloat(watchitems.get(position).currentPrice)/Float.parseFloat(watchitems.get(position).targetEntry))-1)*100;

        if (delta  < 0){
            ((TextView) view.findViewById(R.id.tv_watchlist_returns)).setText(String.format("%.2f", delta)+"%");
            ((TextView) view.findViewById(R.id.tv_watchlist_returns)).setTextColor(getContext().getResources().getColor(R.color.red));
        }else{
            ((TextView) view.findViewById(R.id.tv_watchlist_returns)).setText(String.format("+"+"%.2f", delta)+"%");
            ((TextView) view.findViewById(R.id.tv_watchlist_returns)).setTextColor(getContext().getResources().getColor(R.color.purple_500));
        }
//        ;Log.d("WatchlistAdapterAdapter","Delta: "+delta);
        //((Button) view.findViewById(R.id.btn_buy_hold_sell)).setText(watchitems.get(position).buySellOrHold);
        //((Button) view.findViewById(R.id.btn_buy_hold_sell)).setBackgroundColor(Color.parseColor("#03A9F4"));

        lv_news = view.findViewById(R.id.lv_news);
        news = new ArrayList<News>();
        news.add(new News());
        news.add(new News());
        news.add(new News());
        newsAdapter = new NewsAdapter(rootContext,R.layout.custom_news_item,news);
        lv_news.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
        return view;
    }
}
