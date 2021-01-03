package com.salad.latte

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.salad.latte.Adapters.NewsAdapter
import com.salad.latte.Objects.News
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.BarModel
import java.util.*


class DashboardFragment : Fragment(){

    lateinit var tv_totaltrades :TextView
    lateinit var tv_winrate :TextView
    lateinit var tv_netreturn :TextView
    lateinit var lv_news :ListView
    lateinit var news :ArrayList<News>
    lateinit var newsAdapter :NewsAdapter


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

        lv_news = view.findViewById(R.id.lv_news)
        news = ArrayList<News>()
        news.add(News())
        news.add(News())
        news.add(News())
        newsAdapter = NewsAdapter(context!!,R.layout.custom_news_item,news)
        lv_news.setAdapter(newsAdapter)
        newsAdapter.notifyDataSetChanged()






        return view
    }


    fun initChart(view :View, context : Context) {
        val mBarChart = view.findViewById(R.id.chart_month_by_month) as BarChart

        mBarChart.addBar(BarModel(-2.3f, -0xedcbaa))
        mBarChart.addBar(BarModel("Feb",3500.00f, -0xcbcbaa))
        mBarChart.addBar(BarModel(3.3f, -0xa9cbaa))
        mBarChart.addBar(BarModel(1.1f, -0x78c0aa))
        mBarChart.addBar(BarModel(2.7f, -0xa9480f))
        mBarChart.addBar(BarModel(2f, -0xcbcbaa))
        mBarChart.addBar(BarModel(0.4f, -0xe00b54))
        mBarChart.addBar(BarModel(4f, -0xe45b1a))
        mBarChart.isFocusable = true

        mBarChart.startAnimation()


        val graph = view.findViewById(R.id.graph) as GraphView
        val series: BarGraphSeries<DataPoint> = BarGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(0.0, 10.0),
                DataPoint(1.0, 20.0),
                DataPoint(2.0, 15.0),
                DataPoint(3.0, -7.0),
                DataPoint(4.0, 2.0),
                DataPoint(5.0, 4.0),
                DataPoint(6.0, 7.0)
            )
        )
        graph.addSeries(series)
    }
}
