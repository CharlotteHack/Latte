package com.salad.latte.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Objects.DailyWatchlistItem
import com.salad.latte.R

class DailyWatchlistAdapter(var items :ArrayList<DailyWatchlistItem>, var context :Context) : RecyclerView.Adapter<DailyWatchViewHolder>() {

    public

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWatchViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.custom_daily_stock,parent,false)
        return DailyWatchViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: DailyWatchViewHolder, position: Int) {
        holder.tick.setText(items.get(position).ticker)
    }

    override fun getItemCount(): Int {
        Log.d("DailyWatchlistAdapter","Items: "+items.size)
        return items.size
    }
}

class DailyWatchViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) {
    public var tick = itemView.findViewById<TextView>(R.id.daily_stock_ticker_tv)



}