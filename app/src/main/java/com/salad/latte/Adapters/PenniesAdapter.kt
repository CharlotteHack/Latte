package com.salad.latte.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Objects.DailyWatchlistItem
import com.salad.latte.R

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
        holder.ticker.setText(pennies.get(position).ticker)
    }

    override fun getItemCount(): Int {
        return pennies.size
    }

    class PenniesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        lateinit var ticker :TextView

    }
}