package com.salad.latte.Adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        holder.entryDate.setText(items.get(position).entryDate);
        holder.entryPrice.setText(items.get(position).entryPrice.toString())
        try{
            Picasso.get().load(items.get(position).imgUrl).into(holder.img)
            }
        catch  (e :Exception){
            e.printStackTrace()
        }
        if(items.get(position).exitPrice == 0f){
            holder.currentOrExitPriceLabel.setText("Market Price")
            holder.currentOrexitPrice.setText(items.get(position).currentPrice.toString())
            var returnPercent = (items.get(position).currentPrice-items.get(position).entryPrice)/items.get(position).currentPrice
            holder.return_tv.setText(String.format("%.3f", (returnPercent*100)).toDouble().toString()+"%")
            if(returnPercent > 0){
                holder.return_tv.setTextColor(Color.GREEN)
            }
            else{
                holder.return_tv.setTextColor(Color.RED)
            }
            holder.easyPrint.setText("On "+items.get(position).entryDate+" we bought shares of "+items.get(position).ticker+" priced at \$"+items.get(position).entryPrice+" per share.")
        }
        else{
            holder.currentOrExitPriceLabel.setText(" Exit Price ")
            holder.currentOrexitPrice.setText(items.get(position).exitPrice.toString())
            var returnPercent = (items.get(position).exitPrice-items.get(position).entryPrice)/items.get(position).exitPrice
            if(returnPercent > 0){
                holder.return_tv.setTextColor(Color.GREEN)
            }
            else{
                holder.return_tv.setTextColor(Color.RED)
            }
            holder.return_tv.setText(String.format("%.3f", (returnPercent*100)).toDouble().toString()+"%")
            holder.easyPrint.setText("We sold the shares of "+ items.get(position).ticker+" for \$"+items.get(position).exitPrice+" per share.")

        }
//        holder.setText(items.get(position).ticker)
//        holder.tick.setText(items.get(position).ticker)
//        holder.tick.setText(items.get(position).ticker)
//        holder.tick.setText(items.get(position).ticker)
    }

    override fun getItemCount(): Int {
        Log.d("DailyWatchlistAdapter","Items: "+items.size)
        return items.size
    }
}

class DailyWatchViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) {
    public var tick = itemView.findViewById<TextView>(R.id.daily_stock_ticker_tv)
    public var entryPrice = itemView.findViewById<TextView>(R.id.daily_stock_entry_price_tv)
    public var currentOrexitPrice = itemView.findViewById<TextView>(R.id.daily_stock_current_price_tv)
    public var entryDate = itemView.findViewById<TextView>(R.id.daily_stock_entry_date_tv)
    public var currentOrExitPriceLabel = itemView.findViewById<TextView>(R.id.daily_move_tv)
    public var return_tv = itemView.findViewById<TextView>(R.id.daily_stock_return_tv)
    var easyPrint = itemView.findViewById<TextView>(R.id.tv_easyPrint)

    var img = itemView.findViewById<ImageView>(R.id.daily_stock_iv)
//    public var tick = itemView.findViewById<TextView>(R.id.daily_stock_ticker_tv)




}