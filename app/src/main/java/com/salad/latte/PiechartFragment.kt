package com.salad.latte

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.google.firebase.database.*
import com.salad.latte.Adapters.PieAdapter
import com.salad.latte.GeneratePieData.generatePieData
import com.salad.latte.Objects.Pie


class PiechartFragment : Fragment(){
    lateinit var chart: PieChart
    private var closedPosList :ArrayList<Pie>? = null;
    private lateinit var listViewClosed :ListView;
    lateinit var allocPositons :TextView
    lateinit var mDatabase :DatabaseReference
    lateinit var pieReference :ValueEventListener;
    lateinit var pieAdapter: PieAdapter;
    private var allocationCount = 0
    lateinit var v: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(com.salad.latte.R.layout.fragment_piechart,container,false)
        val bundle = this.arguments
        if (bundle != null) {
            allocationCount = bundle.getInt("allocationCount", 0)
            Log.d("PiechartFragment: ","allocation found!, num allocations: "+allocationCount);

        }
        mDatabase = FirebaseDatabase.getInstance().getReference()

        allocPositons = v.findViewById(R.id.tv_pie_allocated_count)

         chart =  (v.findViewById(R.id.piechart)) as PieChart

        //
        listViewClosed = v.findViewById(com.salad.latte.R.id.lv_piechart_closedPos) as ListView

        closedPosList = ArrayList<Pie>();
        closedPosList!!.addAll(pullPieChart(context,R.layout.custom_pie,listViewClosed,chart));
        pieAdapter = PieAdapter(context!!,R.layout.custom_pie,closedPosList!!)

        var closedAdapter = PieAdapter(
            context!!,
            R.layout.custom_pie,
            closedPosList!!
        );
        closedAdapter.notifyDataSetChanged()
        listViewClosed.adapter = closedAdapter;

        //

        chart.getDescription().isEnabled = false

        //val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")

        //chart.setCenterTextTypeface(tf)
        chart.setCenterText("Allocations: 2% Each")
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

//        chart.setData(generatePieData(context,allocationCount))

        return v
    }

    fun pullPieChart(context: Context?, layout: Int, closedList: ListView, chaa :PieChart ): ArrayList<Pie> {
//        if (pieReference != null) {
//            mDatabase.removeEventListener(pieReference)
//        }
        pieReference = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                closedPosList!!.clear()
                Log.d("FirebaseDB", "Snapshot count: " + snapshot.child("pie").childrenCount)
                for (datasnap in snapshot.child("pie").children) {
                    closedPosList!!.add(
                            Pie(datasnap.child("icon").getValue(String::class.java).toString() + "", datasnap.child("ticker").getValue(String::class.java).toString() + "", datasnap.child("entryDate").getValue(String::class.java).toString() + "", datasnap.child("entryPrice").getValue(String::class.java).toString() + "", datasnap.child("currentPrice").getValue(String::class.java).toString() + "", datasnap.child("allocation").getValue(String::class.java).toString() + "")
                    )
                }
                //

                closedPosList!!.sort()
//                for (item in closedPosList!!) {
//                    Log.d("PieChartFragment", item.ticker)
//                }

                pieAdapter = PieAdapter(context!!, layout, closedPosList!!)
                closedList.adapter = pieAdapter
                allocPositons.text = "Allocated Positions: "+closedPosList!!.size
                chaa.data = generatePieData(context,closedPosList!!.size)
                chaa.invalidate()
                pieAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {}
        }
        mDatabase.addValueEventListener(pieReference)
        Log.d("FirebaseDB", "Results found for Pie: " + closedPosList!!.size)
        return closedPosList!!
    }






}