package com.salad.latte

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Legend
import com.google.firebase.database.*
import com.salad.latte.Adapters.PieAdapter
import com.salad.latte.Objects.Pie
import org.eazegraph.lib.charts.PieChart
import java.util.zip.Inflater

class DailyPieFragment() : Fragment() {

    lateinit var pieChart :PieChart
    lateinit var pieList :ArrayList<Pie>
    lateinit var pieAdapter: PieAdapter;
    lateinit var mDatabase : DatabaseReference
    lateinit var pieReference : ValueEventListener;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_pie_positions,container,false);
        pieChart = view.findViewById(R.id.dailyPie)
        pieList = ArrayList<Pie>()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        pieList!!.addAll(pullPieChart(this,R.layout.custom_pie,listViewClosed,chart,progress_piechart));
        pieAdapter = PieAdapter(this,R.layout.custom_pie,closedPosList!!)
        var closedAdapter = PieAdapter(
            this,
            R.layout.custom_pie,
            closedPosList!!
        );
        closedAdapter.notifyDataSetChanged()
        listViewClosed.adapter = closedAdapter;

        //

        chart.getDescription().isEnabled = false

        //val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")

        //chart.setCenterTextTypeface(tf)
        chart.setCenterText("Allocations: 2% Each Position")
        chart.setCenterTextSize(10f)
        //chart.setCenterTextTypeface(tf)

        // radius of the center hole in percent of maximum radius

        // radius of the center hole in percent of maximum radius
        chart.setHoleRadius(45f)
        chart.setTransparentCircleRadius(50f)

        val l = chart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)



        return view
    }

    fun pullPieChart(context: Context?, layout: Int, closedList: ListView, chaa : com.github.mikephil.charting.charts.PieChart, pie_progress : ProgressBar): ArrayList<Pie> {
        progress_piechart.visibility = View.VISIBLE
//        if (pieReference != null) {
//            mDatabase.removeEventListener(pieReference)
//        }
        pieReference = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                closedPosList!!.clear()
                Log.d("FirebaseDB", "Snapshot count: " + snapshot.child("pie").childrenCount)
                for (datasnap in snapshot.child("pie").children) {
                    //Only include the items that we are actually carrying in our portfolio
                    if(datasnap.child("inOurPortfolio").getValue(Boolean::class.java) as Boolean) {
                        closedPosList!!.add(
                            Pie(datasnap.child("icon").getValue(String::class.java).toString() + "",
                                datasnap.child("ticker").getValue(String::class.java)
                                    .toString() + "",
                                datasnap.child("entryDate").getValue(String::class.java)
                                    .toString() + "",
                                datasnap.child("entryPrice").getValue(String::class.java)
                                    .toString() + "",
                                datasnap.child("currentPrice").getValue(String::class.java)
                                    .toString() + "",
                                datasnap.child("allocation").getValue(String::class.java)
                                    .toString() + ""
                            )
                        )
                    }
                }
                //

                closedPosList!!.sort()
//                for (item in closedPosList!!) {
//                    Log.d("PieChartFragment", item.ticker)
//                }

                pieAdapter = PieAdapter(context!!, layout, closedPosList!!)
                closedList.adapter = pieAdapter
                allocPositons.text = "Allocated Positions: "+closedPosList!!.size
                chaa.data = GeneratePieData.generatePieData(context, closedPosList!!.size / 2)
                chaa.invalidate()
                pieAdapter.notifyDataSetChanged()
                progress_piechart.visibility = View.INVISIBLE

            }

            override fun onCancelled(error: DatabaseError) {}
        }
        mDatabase.addValueEventListener(pieReference)
        Log.d("FirebaseDB", "Results found for Pie: " + closedPosList!!.size)
        return closedPosList!!
    }
}