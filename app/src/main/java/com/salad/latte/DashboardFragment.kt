package com.salad.latte

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.BarModel

class DashboardFragment : Fragment(){

    lateinit var tv_totaltrades :TextView
    lateinit var tv_winrate :TextView
    lateinit var tv_netreturn :TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard,container,false)
        tv_totaltrades = view.findViewById(R.id.tv_totaltrades2)
//        tv_winrate = view.findViewById(R.id.tv_winrate2)
        tv_netreturn = view.findViewById(R.id.tv_netreturn2)
        initChart(view, context!!)




        return view
    }


    fun initChart(view :View, context : Context) {
        val mBarChart = view.findViewById(R.id.chart_month_by_month) as BarChart

        mBarChart.addBar(BarModel(-2.3f, -0xedcbaa))
        mBarChart.addBar(BarModel(2f, -0xcbcbaa))
        mBarChart.addBar(BarModel(3.3f, -0xa9cbaa))
        mBarChart.addBar(BarModel(1.1f, -0x78c0aa))
        mBarChart.addBar(BarModel(2.7f, -0xa9480f))
        mBarChart.addBar(BarModel(2f, -0xcbcbaa))
        mBarChart.addBar(BarModel(0.4f, -0xe00b54))
        mBarChart.addBar(BarModel(4f, -0xe45b1a))

        mBarChart.startAnimation()
    }
}
