package com.salad.latte.DeprecatedClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.salad.latte.Adapters.RecentsAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Watchlist
import com.salad.latte.R

class RecentFragment :Fragment() {


    lateinit var recentsList : ListView
    lateinit var recentItems :ArrayList<Watchlist>
    lateinit var recentsAdapter : RecentsAdapter
    lateinit var firebaseDB : FirebaseDB
    lateinit var recents_spinner: Spinner


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recents,container,false)
        firebaseDB = FirebaseDB();
        recentItems = ArrayList()
        recentsList = view.findViewById<ListView>(R.id.list_recents);
        recents_spinner = view.findViewById(R.id.spinner_recents)
        recentItems.addAll(firebaseDB.pullRecentsData(context!!, R.layout.custom_recents,recentsList))
        Log.d("FirebaseDB2", "Results found for Recents: " + recentItems.size)
        recentsAdapter = RecentsAdapter(context!!, R.layout.custom_recents,recentItems)



        val languages = resources.getStringArray(R.array.spinner_recents)
        if (recents_spinner != null) {
            val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_item, languages)
            recents_spinner.adapter = adapter
        }
        recents_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View?, position: Int, id: Long) {
//                Toast.makeText(context!!,
//                        languages[position], Toast.LENGTH_SHORT).show()

                if(languages[position].contains("All")){

                    recentItems.clear()
                    recentItems.addAll(firebaseDB.pullRecentsData(context!!, R.layout.custom_recents,recentsList))
                    recentsAdapter.notifyDataSetChanged()
                }
                else{
                    var month = languages[position].split(" ")[0]

                    recentItems.clear()
                    recentItems = firebaseDB.pullRecentsDataByDate(context!!, R.layout.custom_historical,recentsList,month);
                    recentsAdapter.notifyDataSetChanged()
                    Log.d("HistoricalFragment: ","Year to search for: "+month)
                    //Log.d("HistoricalFragment: ","# Historical Items: "+historicalItems.size)
                    //Log.d("HistoricalFragment: ","Num exits in the year: "+languages[position]+" : "+historicalItems.size)
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        return view
    }



}