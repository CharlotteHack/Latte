package com.salad.latte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.salad.latte.Adapters.DailyWatchlistAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Dialogs.HowToBuyDialogFragment
import com.salad.latte.Objects.CalculateItem
import com.salad.latte.Objects.DailyWatchlistItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.json.JSONObject


class DailyWatchlistFragment : Fragment() {

    lateinit var dailyWatchRV :RecyclerView
    lateinit var postReference : DatabaseReference
    lateinit var daily_spinner :Spinner
    lateinit var fab_calculate :FloatingActionButton
    lateinit var pb_daily_picks :ProgressBar
    var dailyDates = ArrayList<String>()
    var items = ArrayList<DailyWatchlistItem>()
    var firebaseDB = FirebaseDB()
    lateinit var fragManager : FragmentManager
    lateinit var daily_stock_updatetime :TextView
    lateinit var howto_btn :Button
    lateinit var monthlyItemsObservable :Observable<ArrayList<DailyWatchlistItem>>
    lateinit var monthlyItemObserver :Disposable
    lateinit var getYTD :TextView
    lateinit var getSPYYTD :TextView



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_daily_watchlist, container, false)
        fab_calculate = v.findViewById(R.id.calculate_fab)
        fragManager = fragmentManager!!
        //
//        fab_calculate.visibility = View.VISIBLE
        pb_daily_picks = v.findViewById(R.id.daily_stocks_pb)
        daily_stock_updatetime = v.findViewById(R.id.daily_stock_updatetime)
        howto_btn = v.findViewById(R.id.howtobuy_btn)
        getYTD = v.findViewById(R.id.getytd_tv)
        getSPYYTD = v.findViewById(R.id.spy_tv)
        fab_calculate.setOnClickListener{
            if(firebaseDB.dailyPicks.size > 0) {
                var intent = Intent(requireContext(), CalculateActivity::class.java)
                var calcItems = ArrayList<CalculateItem>()
                firebaseDB.dailyPicks.forEach({
                    var calcItem = CalculateItem(it.imgUrl, it.ticker, it.entryPrice, 0f, 0f, it.allocation)
                    calcItems.add(calcItem)

                })
                intent.putExtra("picks", calcItems)
                startActivity(intent)
            }
            Log.d("DailyWatchlistFragment", "#Items: " + firebaseDB.dailyPicks.size)


//            var calculateDialog = CalculateDialogFragment()
//            calculateDialog.show(fragManager,"CalculateDialog")
        }


        /*
        Create an observable with just item ready to be emitted, 1
         */

        var obserable = Observable.create(ObservableOnSubscribe<String>() { emitter ->
            var test = pullRandomData()
            val queue = Volley.newRequestQueue(context)
            val url = "https://www.google.com"

// Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                    { response ->

                        // Display the first 500 characters of the response string.
                        var rep = "Response is: ${response.substring(0, 500)}"
                        Log.d("DailyWatchlistFragment", "Got response")
                        emitter.onNext(rep)

                    },
                    {
                        var rep = "That didn't work!"
                        Log.d("DailyWatchlistFragment", "didnt get response")
                        emitter.onNext(rep)

                    }
            )
            queue.add(stringRequest);

            /* Do Logic in here */


//            emitter.onNext()
//            emitter.onNext("")
        }
        )
        var observer = obserable.subscribeBy(
                onNext = { Log.d("DailyWatchlistFragment", "emitted response: " + it.toString()) }
        )




    howto_btn.setOnClickListener(View.OnClickListener {


        HowToBuyDialogFragment().show(fragManager, "HowToBuyDialogFragment")
    })
//        Toasty.info(requireContext(),"new picks are displayed after hours (4PM EST) to avoid market volatility").show()
        postReference = Firebase.database.reference
        daily_spinner = v.findViewById(R.id.daily_spinner)
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, dailyDates)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daily_spinner.setAdapter(spinnerAdapter)
        dailyWatchRV = v.findViewById(R.id.daily_watch_rv)
        setSPYYtd()

        firebaseDB.setMonthlyDates(dailyDates, spinnerAdapter, pb_daily_picks, fab_calculate, dailyWatchRV)
        firebaseDB.pullUpdatedDailyPickTime(daily_stock_updatetime)
        firebaseDB.setYTDResults(getYTD)


//        firebaseDB.pullUpdatedYTD(getYTD)

        spinnerAdapter.notifyDataSetChanged()
//        postReference.addValueEventListener(getDailyPickItems(postReference))
//        postReference.child("test").setValue("pewp")


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
    public fun calculateSPYYTD(entry: Double, current: Double) : String {

        return String.format("%.02f", (((current-entry)/entry)*100))
    }
    public fun setSPYYtd(){

        Log.d("LOL22", "crud")
        val url = "https://api.polygon.io/v2/last/trade/SPY?apiKey=vGM47_0HjvEz4RB_05YtzyLdSQhddiKgZHCl0d"



        val queue = Volley.newRequestQueue(activity)

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    var exit = response["results"]
                    var jsonObj = JSONObject(exit.toString())
                    var currentPrice = jsonObj.getDouble("p")
                    Log.d("LOL2", calculateSPYYTD(477.71, currentPrice))
//                    textView.text = "Response: %s".format(response.toString())
                    getSPYYTD.text = "S&P 500 YTD Return: " + calculateSPYYTD(477.71, currentPrice) + "%"
                },
                Response.ErrorListener {
                    // TODO: Handle error
                    Log.d("LOL2", it.message.toString())
                })

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }

    public fun pullRandomData() : String  {
        val queue = Volley.newRequestQueue(context)
        val url = "https://www.google.com"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->

                    // Display the first 500 characters of the response string.
                    var rep = "Response is: ${response.substring(0, 500)}"

                },
                {
                    var rep = "That didn't work!"
                }
        )

// Add the request to the RequestQueue.
        return queue.add(stringRequest).url
    }
    public fun pullDailyDataForDate(dateIn: String){
        var monthlyItemsObservable = Observable.create<ArrayList<DailyWatchlistItem>>{ emitter ->
            items.clear()
            items.addAll(firebaseDB.pullMonthDataForDate(requireContext(), dailyWatchRV, dateIn))
            //Actions happen in here
            emitter.onNext(items)
        }

        monthlyItemObserver = monthlyItemsObservable.subscribeBy(
                onNext = {
                    var adapter = DailyWatchlistAdapter(it, requireContext())
                    dailyWatchRV.layoutManager = LinearLayoutManager(activity)
                    dailyWatchRV.adapter = adapter
                    adapter.notifyDataSetChanged()
                    Log.d("DailyWatchlistFragment", "Observable called for monthly items")
//                    setYTDResults()


                }
        )

    }


}