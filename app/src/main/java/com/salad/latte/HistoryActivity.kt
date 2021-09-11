package com.salad.latte

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.salad.latte.Adapters.HistoricalAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Historical

class HistoryActivity :AppCompatActivity() {

    lateinit var historicalList : ListView
    lateinit var historicalItems :ArrayList<Historical>
    lateinit var firebaseDB: FirebaseDB
    lateinit var historical_spinner: Spinner
    lateinit var historicalAdapter : HistoricalAdapter
    lateinit var historical_progress : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        firebaseDB = FirebaseDB()
        historicalList = findViewById(R.id.list_historical)
        historical_spinner = findViewById(R.id.spinner_history_activity)
        historicalItems = ArrayList<Historical>()
        val languages = resources.getStringArray(R.array.spinner)
        if (historical_spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            historical_spinner.adapter = adapter
        }
        historical_progress = findViewById(R.id.progress_historical)
        historical_spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View?, position: Int, id: Long) {
//                Toast.makeText(context!!,
//                        languages[position], Toast.LENGTH_SHORT).show()

                if(languages[position].contains("All")){

                    historicalItems.clear()
                    historicalItems.addAll(firebaseDB.pullHistoricalDataOnce(baseContext,R.layout.custom_historical,historicalList,historical_progress))
                    historicalAdapter.notifyDataSetChanged()
                }
                else{
                    var year = languages[position].split(" ")[0]

                    historicalItems.clear()
                    historicalItems = firebaseDB.pullHistoricalDataByDateOnce(baseContext,R.layout.custom_historical,historicalList,year,historical_progress);
                    historicalAdapter.notifyDataSetChanged()
                    Log.d("HistoricalFragment: ","Year to search for: "+year)
                    //Log.d("HistoricalFragment: ","# Historical Items: "+historicalItems.size)
                    //Log.d("HistoricalFragment: ","Num exits in the year: "+languages[position]+" : "+historicalItems.size)
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


//        var limit = ArrayList<Historical>()
//        var li = 0;
//        for (hist in historicalItems){
//            if(li < 5) {
//                limit.add(hist)
//                li++
//            }
//        }
        //

        historicalAdapter = HistoricalAdapter(this,R.layout.custom_historical,historicalItems);
        historicalList.adapter = historicalAdapter
        historicalAdapter.notifyDataSetChanged()


    }


}