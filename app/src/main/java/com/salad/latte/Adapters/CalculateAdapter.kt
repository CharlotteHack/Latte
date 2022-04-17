package com.salad.latte.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Objects.CalculateItem
import com.salad.latte.R
import com.squareup.picasso.Picasso
import kotlin.math.floor

class CalculateAdapter(var calculateItems :ArrayList<CalculateItem>, var context : Context) : RecyclerView.Adapter<CalculateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculateViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.custom_calculate_item,parent,false)
        return CalculateViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CalculateViewHolder, position: Int) {
        Picasso.get().load(calculateItems.get(position).imgUrl).into(holder.imgView)
        holder.ticker_tv.setText(calculateItems.get(position).ticker)
        holder.entryPrice_tv.setText(calculateItems.get(position).entryPrice.toString())
        holder.allocation_tv.setText(calculateItems.get(position).allocation.toString()+"%")
        if(calculateItems.get(position).totalCapital == 0f){
            holder.sharesToBuy_tv.setText("--")
        }
        else{
            var totalCap = calculateItems.get(position).totalCapital
            var alloc = floor(calculateItems.get(position).allocation)/100
            var adjustedCapitalAfterAllocation = totalCap*alloc
            var sharesToBuy = floor(adjustedCapitalAfterAllocation/calculateItems.get(position).entryPrice)
            Log.d("CalculateAdapter","Total Cap: "+totalCap+" Alloc: "+alloc+" Adj cap: "+adjustedCapitalAfterAllocation+" Shares to buy: "+sharesToBuy)
            holder.sharesToBuy_tv.setText(sharesToBuy.toString())
        }
    }

    override fun getItemCount(): Int {
        Log.d("CalculateAdapter","Size of items to calculate: "+calculateItems.size)
        return calculateItems.size
    }
}

class CalculateViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var imgView = itemView.findViewById<ImageView>(R.id.calculator_img_iv)
    var ticker_tv = itemView.findViewById<TextView>(R.id.calculator_ticker_tv)
    var entryPrice_tv = itemView.findViewById<TextView>(R.id.calculator_entry_price_tv)
    var sharesToBuy_tv = itemView.findViewById<TextView>(R.id.calculator_shares_to_buy_tv)
    var allocation_tv = itemView.findViewById<TextView>(R.id.calculator_alloc_tv)






}