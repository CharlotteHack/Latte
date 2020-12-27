package com.salad.latte

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.BarModel


class MainFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //var view = inflater.inflate(R.layout.fragment_main,container,false);
        //initChart(view, activity!!.application)
        return view;
    }

    fun initChart(view :View, context :Context){


    }
}