package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Adapters.PenniesAdapter
import com.salad.latte.Objects.DailyWatchlistItem

class PennyFragment :Fragment() {

    lateinit var pennylist : RecyclerView
    lateinit var pennies :ArrayList<DailyWatchlistItem>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_penny,container,false)
        pennylist = v.findViewById(R.id.penny_list)
        var penny = DailyWatchlistItem("","Test",2.0f,0.0f,4.0f,100.0f,"8/30/2022")
        pennies.add(penny)
        var adapter = PenniesAdapter(context!!,R.layout.custom_daily_stock,pennies)
        pennylist.layoutManager = LinearLayoutManager(activity)
        pennylist.adapter = adapter
        adapter.notifyDataSetChanged()
        return v;
    }
}