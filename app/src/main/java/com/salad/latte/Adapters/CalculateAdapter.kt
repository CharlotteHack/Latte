package com.salad.latte.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Objects.CalculateItem
import com.salad.latte.R
import com.squareup.picasso.Picasso

class CalculateAdapter(var calculateItems :ArrayList<CalculateItem>, var context : Context) : RecyclerView.Adapter<CalculateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculateViewHolder {
        val inflater = LayoutInflater.from(context).inflate(R.layout.custom_calculate_item,parent,false)
        return CalculateViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CalculateViewHolder, position: Int) {
        Picasso.get().load(calculateItems.get(position).imgUrl).into(holder.imgView)
        holder.ticker_tv.setText(calculateItems.get(position).ticker)
        holder.entryPrice_tv.setText(calculateItems.get(position).entryPrice.toString())
        if(calculateItems.get(position).totalCapital == 0f){
            holder.sharesToBuy_tv.setText("--")
        }
        else{
            var adjustedCapitalAfterAllocation = calculateItems.get(position).totalCapital*calculateItems.get(position).allocation
            var sharesToBuy = Integer.parseInt((adjustedCapitalAfterAllocation/calculateItems.get(position).entryPrice).toString())
            holder.sharesToBuy_tv.setText(sharesToBuy.toString())
        }
    }

    override fun getItemCount(): Int {
        return calculateItems.size
    }
}

class CalculateViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    lateinit var imgView :ImageView
    lateinit var ticker_tv :TextView
    lateinit var entryPrice_tv :TextView
    lateinit var sharesToBuy_tv :TextView

}