package com.salad.latte.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.salad.latte.GeneratePieData;
import com.salad.latte.Objects.Pie;
import com.salad.latte.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DailyPieAdapter extends RecyclerView.Adapter<DailyPieAdapter.PieViewHolder> {
    ArrayList<Pie> pies;
    Context context;
    int res;
    PieChart pieChart;

    @NonNull
    @Override
    public PieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_pie,parent,false);
        return new PieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PieViewHolder holder, int position) {
        holder.pie_ticker.setText(pies.get(position).ticker);

        if(!pies.get(position).icon.equals("")){
            //((FloatingActionButton) view.findViewById(R.id.floatingActionButton))..setImageURI(watchitems.get(position).icon);
            Picasso.get().load(pies.get(position).icon).into(holder.pie_icon);

        }
        holder.pie_entryDay.setText("Entry Date: "+pies.get(position).entryDate);
        holder.pie_currentPrice.setText("Entry Price: $"+pies.get(position).entryPrice+" | Current Price $"+pies.get(position).currentPrice);
        float delta = ((Float.parseFloat(pies.get(position).currentPrice)/Float.parseFloat(pies.get(position).entryPrice))-1)*100;

        if (delta  < 0){
            holder.pie_delta.setText(String.format("%.2f", delta)+"%");
            holder.pie_delta.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            holder.pie_delta.setText(String.format("+"+"%.2f", delta)+"%");
            holder.pie_delta.setTextColor(context.getResources().getColor(R.color.purple_500));
        }
    }

    @Override
    public int getItemCount() {
        return pies.size();
    }


    public DailyPieAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Pie> objects) {
        super();
        pies = objects;
        this.context = context;
        this.res = resource;
    }



    public void setPieChart(PieChart pieChart) {
        this.pieChart = pieChart;
    }






    class PieViewHolder extends RecyclerView.ViewHolder {

        TextView pie_ticker;
        TextView pie_entryDay;
        TextView pie_currentPrice;
        FloatingActionButton pie_icon;
        TextView pie_delta;

        public PieViewHolder(@NonNull View itemView) {
            super(itemView);
            pie_ticker = itemView.findViewById(R.id.tv_pie_ticker);
            pie_entryDay = itemView.findViewById(R.id.tv_pie_entryDate);
            pie_currentPrice = itemView.findViewById(R.id.tv_pie_entry_current);
            pie_delta = itemView.findViewById(R.id.tv_pie_delta);
            pie_icon = itemView.findViewById(R.id.fab_pie_icon);

        }
    }
}
