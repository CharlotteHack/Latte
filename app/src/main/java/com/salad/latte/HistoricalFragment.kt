package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.salad.latte.Adapters.HistoricalAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Historical

class HistoricalFragment : Fragment(){

    lateinit var historicalList :ListView
    lateinit var historicalItems :ArrayList<Historical>
    lateinit var firebaseDB: FirebaseDB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseDB = FirebaseDB()
        val view = inflater.inflate(R.layout.fragment_historical,container,false)
        historicalList = view.findViewById(R.id.list_historical)
        historicalItems = ArrayList<Historical>()
        historicalItems.clear()
        historicalItems.addAll(firebaseDB.pullHistoricalData(context!!,R.layout.custom_historical,historicalList))
        //

        val historicalAdapter = HistoricalAdapter(context!!,R.layout.custom_historical,historicalItems);
        historicalList.adapter = historicalAdapter
        historicalAdapter.notifyDataSetChanged()
        return view
    }
}
