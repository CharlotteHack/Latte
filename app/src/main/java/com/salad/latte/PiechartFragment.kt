package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.salad.latte.Adapters.PieAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.GeneratePieData.generatePieData
import com.salad.latte.Objects.Pie


class PiechartFragment : Fragment(){
    private var chart: PieChart? = null
    private var closedPosList :ArrayList<Pie>? = null;
    private lateinit var firebaseDB :FirebaseDB
    private lateinit var listViewClosed :ListView;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.salad.latte.R.layout.fragment_piechart,container,false)
        firebaseDB = FirebaseDB()

        val chart =  (view.findViewById(com.salad.latte.R.id.piechart)) as PieChart



        //
        listViewClosed = view.findViewById(com.salad.latte.R.id.lv_piechart_closedPos) as ListView

        closedPosList = ArrayList<Pie>();
        closedPosList!!.addAll(firebaseDB.pullPieChart(context,R.layout.custom_pie,listViewClosed,chart));

        var closedAdapter = PieAdapter(
            context!!,
            R.layout.custom_pie,
            closedPosList!!
        );
        closedAdapter.notifyDataSetChanged()
        listViewClosed.adapter = closedAdapter;

        //



        return view
    }




}