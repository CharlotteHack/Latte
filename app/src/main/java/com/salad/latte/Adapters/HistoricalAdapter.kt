package com.salad.latte.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.salad.latte.Objects.Historical
import com.salad.latte.R
import java.math.MathContext
//
class HistoricalAdapter(private val con :Context,private val res :Int,private val items :ArrayList<Historical>) :ArrayAdapter<Historical>(con,res,items) {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(res,parent,false) as View
        val item = items.get(position)
        val tv_ticker  = view.findViewById<TextView>(R.id.tv_historical_ticker)
        tv_ticker.setText(item.ticker)


        val tv_period = view.findViewById<TextView>(R.id.tv_period)
        tv_period.setText("Period: "+item.period)

        val tv_equity = view.findViewById<TextView>(R.id.tv_equity)
        tv_equity.setText(item.equity)

        val tv_alloc = view.findViewById<TextView>(R.id.tv_historical_allocation)
        tv_alloc.setText("Allocation: "+item.allocation+"% of portfolio")

        val tv_fees = view.findViewById<TextView>(R.id.tv_fees)
        tv_fees.setText(item.fees)

        val tv_returns = view.findViewById<TextView>(R.id.tv_returns)
        tv_returns.setText(item.returns)

        val tv_entryPrice = view.findViewById<TextView>(R.id.tv_historical_entryPrice)
        tv_entryPrice.setText("$"+item.entryPrice)

        val tv_exitPrice = view.findViewById<TextView>(R.id.tv_historical_exitPrice)
        tv_exitPrice.setText("$"+item.exitPrice)

        val tv_equity_percent_return = view.findViewById<TextView>(R.id.tv_equity_percent_return)
        var delta = (item.exitPrice.toDouble()/item.entryPrice.toDouble())-1
        tv_equity_percent_return.setText(delta.toBigDecimal().round(MathContext(2)).toString()+"%")
        val fab_percent = view.findViewById<FloatingActionButton>(R.id.fab_return_indicator)
//        Log.d("HistoricalAdapter","Delta: "+delta)
//        Log.d("HistoricalAdapter: ","Ticker: "+item.ticker+" Exit Date: "+item.getExitDate())
        if (delta < 0) {
            fab_percent.setImageResource(R.drawable.downarrow)
//            fab_percent.setBackgroundTintList(ColorStateList.valueOf(context!!.getResources().getColor(R.color.red)));
        } else{
            fab_percent.setImageResource(R.drawable.uparrow)
            fab_percent.setBackgroundTintList(ColorStateList.valueOf(context!!.getResources().getColor(R.color.purple_500)));
        }

        var tv_historical_dividends = view.findViewById<TextView>(R.id.tv_historical_dividends)
        tv_historical_dividends.setText("Dividend Earned: "+item.getDividendsPercentSum()+"%")
//        fab_percent.setText("Period: 10/13/2020 - 10/15/2020")


        return view
    }

    override fun getCount(): Int {
        return items.size
    }
}