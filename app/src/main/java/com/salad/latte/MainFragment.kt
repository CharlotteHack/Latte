package com.salad.latte

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils


class MainFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_main,container,false);
        initChart(view, activity!!.application)
        return view;
    }

    fun initChart(view :View, context :Context){
        var mChart = view.findViewById(R.id.chart_month_by_month) as BarChart;
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        val values: ArrayList<BarEntry> = ArrayList()
        values.add(BarEntry(1.1f, 50f))
//        Display the data
        val set1: BarDataSet
        if (mChart.data != null &&
            mChart.data.dataSetCount > 0
        ) {
            set1 = mChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "Monthly Return")
            set1.setDrawIcons(false)
            set1.label = "okur"
            //set1.enableDashedLine(10f, 5f, 0f)
            //set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = Color.DKGRAY
            //set1.setCircleColor(Color.DKGRAY)
            //set1.lineWidth = 1f
            //set1.circleRadius = 3f
            //set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            //set1.setDrawFilled(true)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            if (Utils.getSDKInt() >= 18) {
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
                //set1.fillDrawable = drawable
            } else {
                //set1.fillColor = Color.DKGRAY
            }
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.dataSetLabels.set(0,"ok")
            mChart.data = data
        }
    }
}