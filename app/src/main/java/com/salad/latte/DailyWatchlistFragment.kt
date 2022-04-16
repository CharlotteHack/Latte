package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Adapters.DailyWatchlistAdapter
import com.salad.latte.Objects.DailyWatchlistItem

class DailyWatchlistFragment : Fragment() {

    lateinit var dailyWatchRV :RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_daily_watchlist,container,false)
        dailyWatchRV = v.findViewById(R.id.daily_watch_rv)
        var items = ArrayList<DailyWatchlistItem>()
        items.add(DailyWatchlistItem("","MSFT",0.0f,0.0f,0.0f))
        items.add(DailyWatchlistItem("","MSFT",0.0f,0.0f,0.0f))
        items.add(DailyWatchlistItem("","MSFT",0.0f,0.0f,0.0f))
        items.add(DailyWatchlistItem("","MSFT",0.0f,0.0f,0.0f))
        items.add(DailyWatchlistItem("","MSFT",0.0f,0.0f,0.0f))
        var adapter = DailyWatchlistAdapter(items,requireContext())
        dailyWatchRV.layoutManager = LinearLayoutManager(activity)
        dailyWatchRV.adapter = adapter
        adapter.notifyDataSetChanged()
        return v;
    }
}