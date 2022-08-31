package com.salad.latte.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Objects.DailyWatchlistItem
import com.salad.latte.R
import com.squareup.picasso.Picasso

class PenniesAdapter (con : Context, reslay :Int, pennyList :ArrayList<DailyWatchlistItem>) : RecyclerView.Adapter<PenniesAdapter.PenniesViewHolder>() {

    lateinit var pennies :ArrayList<DailyWatchlistItem>
    var res = 0;
    lateinit var context :Context
    init {
        pennies = pennyList
        res = reslay
        context = con
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenniesViewHolder {

        return PenniesViewHolder(LayoutInflater.from(context).inflate(res,parent,false))
    }

    override fun onBindViewHolder(holder: PenniesViewHolder, position: Int) {
        var items = pennies
        holder.ticker.setText(items.get(position).ticker)
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
    }

    override fun getItemCount(): Int {
        return pennies.size
    }

    class PenniesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


        public var ticker = itemView.findViewById<TextView>(R.id.daily_stock_ticker_tv)
        public var entryPrice = itemView.findViewById<TextView>(R.id.daily_stock_entry_price_tv)
        public var currentOrexitPrice = itemView.findViewById<TextView>(R.id.daily_stock_current_price_tv)
        public var entryDate = itemView.findViewById<TextView>(R.id.daily_stock_entry_date_tv)
        public var currentOrExitPriceLabel = itemView.findViewById<TextView>(R.id.daily_move_tv)
        public var return_tv = itemView.findViewById<TextView>(R.id.daily_stock_return_tv)
        var easyPrint = itemView.findViewById<TextView>(R.id.tv_easyPrint)

        var img = itemView.findViewById<ImageView>(R.id.daily_stock_iv)



    }
}