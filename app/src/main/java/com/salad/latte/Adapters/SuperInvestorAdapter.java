package com.salad.latte.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.salad.latte.Objects.SuperInvestor.SuperInvestor;
import com.salad.latte.R;

import java.util.ArrayList;
import java.util.List;

public class SuperInvestorAdapter extends ArrayAdapter<SuperInvestor> {

    ArrayList<SuperInvestor> superInvestors;
    Context ctx;
    int res;
    public SuperInvestorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SuperInvestor> objects) {
        super(context, resource, objects);
        superInvestors = objects;
        ctx = context;
        res = resource;
    }

    @Override
    public int getCount() {
        return superInvestors.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = ((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_superinvestor,parent,false);

//        ((TextView) view.findViewById(R.id.tv_period_recents)).setText("Entry date: "+watchitems.get(position).entryDate);
//        ((TextView) view.findViewById(R.id.tv_recents_allocation)).setText(watchitems.get(position).allocation);
//        ((TextView) view.findViewById(R.id.tv_recents_ticker)).setText(watchitems.get(position).ticker);
//        ((TextView) view.findViewById(R.id.tv_recents_entryPrice)).setText(watchitems.get(position).targetEntry);

        return view;
    }
}
