package com.salad.latte.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Objects.DailyWatchlistHistoricalItem
import com.salad.latte.R
import com.squareup.picasso.Picasso

class CustomDailyHistoricalAdapter(var items: ArrayList<DailyWatchlistHistoricalItem>, var context: Context) : RecyclerView.Adapter<DailyHistoricalViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHistoricalViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.custom_daily_historical_stock, parent, false)
        return DailyHistoricalViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: DailyHistoricalViewHolder, position: Int) {
        holder.ticker.setText(items.get(position).ticker)
        holder.date.setText(items.get(position).dt)
        try {
            Picasso.get().load(items.get(position).imgUrl).into(holder.img)
        }
        catch (e :Exception){
            e.printStackTrace()
        }
        var exit = items.get(position).exitPoint.toDouble()
        var entry = items.get(position).entryPoint.toDouble()
        var ret = ((exit-entry)/entry)*100
        if(ret > 0){
            holder.returnTV.setTextColor(Color.GREEN)

        }else{
            holder.returnTV.setTextColor(context.resources.getColor(R.color.red))

        }
        val formatTotalReturn = String.format("%.02f", ret)

        holder.returnTV.setText(formatTotalReturn + "%");


    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class DailyHistoricalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var img = itemView.findViewById<ImageView>(R.id.custom_daily_historical_ticker_iv)
     var ticker = itemView.findViewById<TextView>(R.id.custom_daily_historical_ticker)
     var date = itemView.findViewById<TextView>(R.id.custom_daily_historical_date)
     var returnTV = itemView.findViewById<TextView>(R.id.custom_daily_historical_return)






}