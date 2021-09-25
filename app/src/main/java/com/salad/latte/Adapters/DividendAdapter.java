package com.salad.latte.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.salad.latte.Database.FirebaseDB;
import com.salad.latte.Objects.Dividend;
import com.salad.latte.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DividendAdapter extends ArrayAdapter<Dividend> {

    Context context;
    int res;
    ArrayList<Dividend> dividends;
    public DividendAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Dividend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
        this.dividends = objects;
    }

    @Override
    public int getCount() {
        Log.d("DividendAdapter","Dividend size: "+dividends.size());
        return dividends.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_dividends,parent,false);
        ((TextView) v.findViewById(R.id.tv_period_dividend)).setText("Dividend date: "+dividends.get(position).getDate());

        ((TextView) v.findViewById(R.id.tv_dividend_entryPrice)).setText("$"+ FirebaseDB.getEntryPriceForStock(dividends.get(position).getTicker()));
        Log.d("DividendAdapter","Entry Price: "+FirebaseDB.getEntryPriceForStock(dividends.get(position).getTicker()));
        ((TextView) v.findViewById(R.id.tv_dividend_entryPrice)).setText("$"+ FirebaseDB.getCurrentPriceForStock(dividends.get(position).getTicker()));
        ((TextView) v.findViewById(R.id.tv_equity_percent_return_dividend)).setText(dividends.get(position).getDividendAmount()+" per share");
        ((TextView) v.findViewById(R.id.tv_dividend_ticker)).setText(dividends.get(position).getTicker());

        return v;

    }

}
