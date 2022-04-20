package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Adapters.CustomDailyHistoricalAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.DailyWatchlistHistoricalItem

class DailyHistoricalFragment : Fragment() {
    lateinit var historical_daily_performance_tv :TextView
    lateinit var historical_daily_pb :ProgressBar
    lateinit var historical_daily_performance_rv :RecyclerView
    lateinit var customDailyHistoricalAdapter: CustomDailyHistoricalAdapter
    var items = ArrayList<DailyWatchlistHistoricalItem>()
    var firebaseDB = FirebaseDB()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_historical_daily_performance,container,false)
        historical_daily_performance_rv = v.findViewById(R.id.historical_daily_performance_rv)
        historical_daily_performance_tv = v.findViewById(R.id.historical_daily_performance_tv)
        historical_daily_performance_rv.layoutManager = LinearLayoutManager(activity)
        historical_daily_pb = v.findViewById(R.id.historical_daily_pb)
        customDailyHistoricalAdapter = CustomDailyHistoricalAdapter(items,requireContext());
        items.addAll(firebaseDB.pullDailyHistoricalItems(requireContext(),historical_daily_performance_rv,historical_daily_performance_tv,historical_daily_pb))
//        historical_daily_performance_rv.adapter = customDailyHistoricalAdapter
//        customDailyHistoricalAdapter.notifyDataSetChanged()
        return v


    }

}