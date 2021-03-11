package com.salad.latte

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.salad.latte.Adapters.NewsAdapter
import com.salad.latte.Adapters.WatchListAdapter
import com.salad.latte.Objects.News
import com.salad.latte.Objects.Watchlist
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.BarModel
import java.util.*


class DashboardFragment : Fragment(){

    lateinit var tv_totaltrades :TextView
    lateinit var tv_winrate :TextView
    lateinit var tv_netreturn :TextView


    lateinit var gv_watchlist :GridView
    lateinit var watchitems :ArrayList<Watchlist>
    lateinit var watchAdapter :WatchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard,container,false)
//        tv_totaltrades = view.findViewById(R.id.tv_totaltrades2)
//        tv_winrate = view.findViewById(R.id.tv_winrate2)
//        tv_netreturn = view.findViewById(R.id.tv_netreturn2)



        gv_watchlist = view.findViewById(R.id.gv_watchlist)
        watchitems = ArrayList<Watchlist>()
        var watchlistItem = Watchlist();
        watchlistItem.icon = "https://c0.klipartz.com/pngpicture/203/134/gratis-png-visa.png"
        watchlistItem.ticker = "VISA (V)"
        watchlistItem.allocation = "Allocation: 2%"
        watchlistItem.targetEntry = "Target Entry: $100.71"
        watchlistItem.buySellOrHold = "HOLD"
        watchlistItem.entryDate = "12/31"
        watchitems.add(Watchlist("https://e7.pngegg.com/pngimages/910/492/png-clipart-mastercard-logo-credit-card-visa-brand-mastercard-text-label.png","MA","Target Entry: $336.00",
        "Allocation: 2%","HOLD","%8.01"));




        watchitems.add(watchlistItem)
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())//
        watchAdapter = WatchListAdapter(context!!,R.layout.custom_news_item,watchitems)
        gv_watchlist.setAdapter(watchAdapter)
        watchAdapter.notifyDataSetChanged()






        return view
    }



}
