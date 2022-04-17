package com.salad.latte.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Objects.DailyWatchlistItem
import com.salad.latte.R
import com.squareup.picasso.Picasso

class DailyWatchlistAdapter(var items :ArrayList<DailyWatchlistItem>, var context :Context) : RecyclerView.Adapter<DailyWatchViewHolder>() {

    public

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWatchViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.custom_daily_stock,parent,false)
        return DailyWatchViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: DailyWatchViewHolder, position: Int) {
        holder.tick.setText(items.get(position).ticker)
        holder.allocation.setText(items.get(position).allocation.toString()+"%");
        holder.entryPrice.setText(items.get(position).entryPrice.toString())
        Picasso.get().load(items.get(position).imgUrl).into(holder.img)
        if(items.get(position).exitPrice == 0f){
            holder.currentOrExitPrice.setText("Current Price")
        }
        else{
            holder.currentOrExitPrice.setText("Exit Price")
        }
        holder.return_tv.setText("%")
//        holder.setText(items.get(position).ticker)
//        holder.tick.setText(items.get(position).ticker)
//        holder.tick.setText(items.get(position).ticker)
//        holder.tick.setText(items.get(position).ticker)
    }

    override fun getItemCount(): Int {
//        Log.d("FirebaseDB","Items: "+items.size)
        return items.size
    }
}

class DailyWatchViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) {
    public var tick = itemView.findViewById<TextView>(R.id.daily_stock_ticker_tv)
    public var entryPrice = itemView.findViewById<TextView>(R.id.daily_stock_entry_price_tv)
//    public var exitPrice = itemView.findViewById<TextView>(R.id.)
    public var allocation = itemView.findViewById<TextView>(R.id.daily_stock_allocation_tv)
    public var currentOrExitPrice = itemView.findViewById<TextView>(R.id.daily_move_tv)
    public var return_tv = itemView.findViewById<TextView>(R.id.daily_stock_return_tv)

    var img = itemView.findViewById<ImageView>(R.id.daily_stock_iv)
//    public var tick = itemView.findViewById<TextView>(R.id.daily_stock_ticker_tv)




}