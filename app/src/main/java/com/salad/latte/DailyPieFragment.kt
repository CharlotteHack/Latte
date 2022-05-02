package com.salad.latte

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.google.firebase.database.*
import com.salad.latte.Adapters.DailyPieAdapter
import com.salad.latte.Objects.Pie

class DailyPieFragment() : Fragment() {

    lateinit var pieChart :PieChart
    lateinit var pieList :ArrayList<Pie>
//    lateinit var pieAdapter: PieAdapter;
    lateinit var mDatabase : DatabaseReference
    lateinit var pieReference : ValueEventListener;
    lateinit var pieRecyclerView :RecyclerView
    lateinit var pieProgBar :ProgressBar
    lateinit var pieOpenPositions :TextView
    lateinit var pieTotalReturn : TextView
//    lateinit var chart: com.github.mikephil.charting.charts.PieChart


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_pie_positions, container, false);
        pieChart = view.findViewById(R.id.dailyPie) as PieChart
        pieList = ArrayList<Pie>()
        pieRecyclerView = view.findViewById(R.id.pieRecyclerView)
        mDatabase = FirebaseDatabase.getInstance().getReference()
        pieProgBar = view.findViewById(R.id.pieProgBar)
        pieOpenPositions = view.findViewById(R.id.pie_num_open_pos)
        pieTotalReturn = view.findViewById(R.id.pie_total_return)

//        pieAdapter = PieAdapter(context!!,R.layout.custom_pie,pieList!!)
        pieRecyclerView.layoutManager = LinearLayoutManager(activity)

        var closedAdapter = DailyPieAdapter(
            context!!,
            R.layout.custom_pie,
            pieList!!
        );
        closedAdapter.notifyDataSetChanged()
        pieRecyclerView.adapter = closedAdapter;
        pieList!!.addAll(
            pullPieChart(
                context,
                R.layout.custom_pie,
                closedAdapter,
                pieChart,
                pieProgBar
            )
        );

        //

        pieChart.getDescription().isEnabled = false

        //val tf = Typeface.createFromAsset(context!!.assets, "OpenSans-Light.ttf")

        //chart.setCenterTextTypeface(tf)
        pieChart.setCenterText("Allocations")
        pieChart.setCenterTextSize(10f)
        //chart.setCenterTextTypeface(tf)

        // radius of the center hole in percent of maximum radius

        // radius of the center hole in percent of maximum radius
        pieChart.setHoleRadius(45f)
        pieChart.setTransparentCircleRadius(50f)

        val l = pieChart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)



        return view
    }

    fun pullPieChart(
        context: Context?,
        layout: Int,
        recyclerViewAdapter: DailyPieAdapter,
        chaa: com.github.mikephil.charting.charts.PieChart,
        pie_progress: ProgressBar
    ): ArrayList<Pie> {
        pieProgBar.visibility = View.VISIBLE
        pieRecyclerView.visibility = View.INVISIBLE
//        if (pieReference != null) {
//            mDatabase.removeEventListener(pieReference)
//        }
        pieReference = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var pieReturn = 0f
                pieList!!.clear()
                Log.d(
                    "DailyPieFragment",
                    "Snapshot count: " + snapshot.child("daily_picks").childrenCount
                )
                for (datasnap in snapshot.child("daily_picks").children) {
                    var key = datasnap.key!!
//                    Log.d("DailyPieFragment","Key date: "+key)

                    for ( innerData in snapshot.child("daily_picks").child(key).children){
                        var ticker = innerData.key!!

                        var exitPrice = snapshot.child("daily_picks").child(key).child(ticker).child(
                            ticker
                        ).child("exitPrice").getValue(Float::class.java)
//                        Log.d("DailyPieFragment","Exit price for :"+ticker+" -> "+exitPrice)
                        if(exitPrice == 0f){
                            //We are still in this position, add to allocator
//                                Log.d("DailyPieFragment","Adding ticker to list: "+ticker)
                            var entryPrice = snapshot.child("daily_picks").child(key).child(ticker).child(
                                ticker
                            ).child("entryPrice").getValue(Float::class.java)
                            var currentPrice = snapshot.child("daily_picks").child(key).child(ticker).child(
                                ticker
                            ).child("currentPrice").getValue(Float::class.java)
                            val ret: Float = (currentPrice!! - entryPrice!!) / entryPrice!!
                            pieReturn = pieReturn + ret

                            pieList!!.add(
                                Pie(
                                    snapshot.child("daily_picks").child(key).child(ticker).child(
                                        ticker
                                    ).child("imgUrl").getValue(String::class.java),
                                    snapshot.child("daily_picks").child(key).child(ticker).child(
                                        ticker
                                    ).child("ticker").getValue(String::class.java),
                                    snapshot.child("daily_picks").child(key).child(ticker).child(
                                        ticker
                                    ).child("date").getValue(String::class.java),
                                    snapshot.child("daily_picks").child(key).child(ticker).child(
                                        ticker
                                    ).child("entryPrice").getValue(Float::class.java).toString(),
                                    snapshot.child("daily_picks").child(key).child(ticker).child(
                                        ticker
                                    ).child("currentPrice").getValue(Float::class.java).toString(),
                                    snapshot.child("daily_picks").child(key).child(ticker).child(
                                        ticker
                                    ).child("allocation").getValue(Float::class.java).toString()
                                )
                            )
                        }
                    }
                    //Only include the items that we are actually carrying in our portfolio
                }
                //

                pieList!!.sort()
//                for (item in closedPosList!!) {
//                    Log.d("PieChartFragment", item.ticker)
//                }

//                pieAdapter = PieAdapter(context!!, layout, pieList!!)
//                closedList.adapter = pieAdapter
//                allocPositons.text = "Allocated Positions: "+pieList!!.size
                chaa.data = GeneratePieData.generatePieData(context, pieList!!.size / 2)
                chaa.invalidate()
//                pieAdapter.notifyDataSetChanged()
                recyclerViewAdapter.notifyDataSetChanged()

                pieProgBar.visibility = View.INVISIBLE

                pieRecyclerView.visibility = View.VISIBLE
                pieOpenPositions.setText(pieList!!.size.toString())
                pieTotalReturn.setText(String.format("%.02f", pieReturn * 100) + "%")


                Log.d("DailyPieFragment", "Results found for Pie: " + pieList!!.size)

            }

            override fun onCancelled(error: DatabaseError) {}
        }
        mDatabase.addValueEventListener(pieReference)
        return pieList!!
    }
}