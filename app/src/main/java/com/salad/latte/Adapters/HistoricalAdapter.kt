package com.salad.latte.Adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.salad.latte.Objects.Historical
import com.salad.latte.R

class HistoricalAdapter(private val con :Context,private val res :Int,private val items :ArrayList<Historical>) :ArrayAdapter<Historical>(con,res,items) {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(res,parent,false) as View
        val item = items.get(position)
        val tv_ticker  = view.findViewById<TextView>(R.id.tv_historical_ticker)
        tv_ticker.setText(item.ticker)


        val tv_period = view.findViewById<TextView>(R.id.tv_period)
        tv_period.setText(item.period)

        val tv_equity = view.findViewById<TextView>(R.id.tv_equity)
        tv_equity.setText(item.equity)

        val tv_alloc = view.findViewById<TextView>(R.id.tv_historical_allocation)
        tv_alloc.setText(item.allocation)

        val tv_fees = view.findViewById<TextView>(R.id.tv_fees)
        tv_fees.setText(item.fees)

        val tv_returns = view.findViewById<TextView>(R.id.tv_returns)
        tv_returns.setText(item.returns)

        val tv_entryPrice = view.findViewById<TextView>(R.id.tv_historical_entryPrice)
        tv_entryPrice.setText(item.entryPrice)

        val tv_exitPrice = view.findViewById<TextView>(R.id.tv_historical_exitPrice)
        tv_exitPrice.setText(item.exitPrice)

        val tv_equity_percent_return = view.findViewById<TextView>(R.id.tv_equity_percent_return)
        tv_equity_percent_return.setText(item.percentReturn)

        val fab_percent = view.findViewById<FloatingActionButton>(R.id.fab_return_indicator)
//        fab_percent.setText("Period: 10/13/2020 - 10/15/2020")


        return view
    }

    override fun getCount(): Int {
        return items.size
    }
}