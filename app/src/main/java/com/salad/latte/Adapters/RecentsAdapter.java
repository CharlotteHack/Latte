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
import com.salad.latte.RecentMovesActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecentsAdapter extends ArrayAdapter<Watchlist> {

    ArrayList<Watchlist> watchitems;
    RecentMovesActivity rootContext;
    ListView watchlist;

    ListView lv_news;
    ArrayList<News> news;

    public RecentsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Watchlist> objects) {
        super(context, resource, objects);
        watchitems = objects;
        rootContext = (RecentMovesActivity) context;
    }

    @Override
    public int getCount() {
        return watchitems.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = ((LayoutInflater)rootContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_recents,parent,false);

        ((TextView) view.findViewById(R.id.tv_period_recents)).setText("Entry date: "+watchitems.get(position).entryDate);
        ((TextView) view.findViewById(R.id.tv_recents_allocation)).setText(watchitems.get(position).allocation);
        ((TextView) view.findViewById(R.id.tv_recents_ticker)).setText(watchitems.get(position).ticker);
        ((TextView) view.findViewById(R.id.tv_recents_entryPrice)).setText(watchitems.get(position).targetEntry);

        return view;
    }
}
