package com.salad.latte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.salad.latte.Adapters.DailyWatchlistAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Dialogs.CalculateDialogFragment
import com.salad.latte.Objects.DailyWatchlistItem


class DailyWatchlistFragment : Fragment() {

    lateinit var dailyWatchRV :RecyclerView
    lateinit var postReference : DatabaseReference
    lateinit var daily_spinner :Spinner
    lateinit var fab_calculate :FloatingActionButton
    var dailyDates = ArrayList<String>()
    var items = ArrayList<DailyWatchlistItem>()
    var firebaseDB = FirebaseDB()
    lateinit var fragManager : FragmentManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_daily_watchlist, container, false)
        fab_calculate = v.findViewById(R.id.calculate_fab)
        fragManager = fragmentManager!!
        //

        fab_calculate.setOnClickListener{
        var intent = Intent(requireContext(),CalculateActivity::class.java)
            intent.putExtra("picks",items)
            startActivity(intent)

//            var calculateDialog = CalculateDialogFragment()
//            calculateDialog.show(fragManager,"CalculateDialog")
        }
        postReference = Firebase.database.reference
        daily_spinner = v.findViewById(R.id.daily_spinner)
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, dailyDates)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daily_spinner.setAdapter(spinnerAdapter)
        firebaseDB.setDailyDates(dailyDates,spinnerAdapter)

        spinnerAdapter.notifyDataSetChanged()
//        postReference.addValueEventListener(getDailyPickItems(postReference))
//        postReference.child("test").setValue("pewp")
        dailyWatchRV = v.findViewById(R.id.daily_watch_rv)


//        pullDailyDataForDate(daily_spinner.selectedItem.toString())
        daily_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pullDailyDataForDate(parent!!.getItemAtPosition(position).toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return v;
    }



    public fun pullDailyDataForDate(dateIn :String){
        Log.d("DailyWatchlistFragment", postReference.child("daily_picks").database.toString() + "")
        items.clear()
        items.addAll(firebaseDB.pullDailyDataForDate(requireContext(), dailyWatchRV, dateIn))
        var adapter = DailyWatchlistAdapter(items, requireContext())
        dailyWatchRV.layoutManager = LinearLayoutManager(activity)
        dailyWatchRV.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}