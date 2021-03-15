package com.salad.latte.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.salad.latte.GeneratePieData;
import com.salad.latte.Objects.Pie;
import com.salad.latte.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PieAdapter extends ArrayAdapter<Pie> {
    ArrayList<Pie> pies;
    Context context;
    int res;
    PieChart pieChart;
    public PieAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Pie> objects) {
        super(context, resource, objects);
        pies = objects;
        this.context = context;
        this.res = resource;
    }

    @Override
    public int getCount() {
        return pies.size();
    }

    public void setPieChart(PieChart pieChart) {
        this.pieChart = pieChart;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
//        this.pieChart.setData(GeneratePieData.generatePieData(context,getCount()));

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_pie,parent,false);
        ((TextView) v.findViewById(R.id.tv_pie_ticker)).setText(pies.get(position).ticker);

        if(!pies.get(position).icon.equals("")){
            //((FloatingActionButton) view.findViewById(R.id.floatingActionButton))..setImageURI(watchitems.get(position).icon);
            Picasso.get().load(pies.get(position).icon).into(((FloatingActionButton) v.findViewById(R.id.fab_pie_icon)));

        }
        ((TextView) v.findViewById(R.id.tv_pie_entryDate)).setText("Entry Date: "+pies.get(position).entryDate);
        ((TextView) v.findViewById(R.id.tv_pie_entry_current)).setText("Entry Price: $"+pies.get(position).entryPrice+" | Current Price $"+pies.get(position).currentPrice);
        ((TextView) v.findViewById(R.id.tv_pie_ticker)).setText(pies.get(position).ticker);
        float delta = ((Float.parseFloat(pies.get(position).currentPrice)/Float.parseFloat(pies.get(position).entryPrice))-1)*100;

        if (delta  < 0){
            ((TextView) v.findViewById(R.id.tv_pie_delta)).setText(String.format("-"+"%.2f", delta)+"%");
            ((TextView) v.findViewById(R.id.tv_pie_delta)).setTextColor(getContext().getResources().getColor(R.color.red));
        }else{
            ((TextView) v.findViewById(R.id.tv_pie_delta)).setText(String.format("+"+"%.2f", delta)+"%");
            ((TextView) v.findViewById(R.id.tv_pie_delta)).setTextColor(getContext().getResources().getColor(R.color.purple_500));
        }
        return v;

    }
}
