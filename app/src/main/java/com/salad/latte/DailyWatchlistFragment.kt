package com.salad.latte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.salad.latte.Adapters.DailyWatchlistAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Dialogs.CalculateDialogFragment
import com.salad.latte.Dialogs.HowToBuyDialogFragment
import com.salad.latte.Objects.CalculateItem
import com.salad.latte.Objects.DailyWatchlistItem
import com.salad.latte.Objects.User
import es.dmoral.toasty.Toasty
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlin.math.roundToInt


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
        fab_calculate.setOnClickListener{
            if(firebaseDB.dailyPicks.size > 0) {
                var intent = Intent(requireContext(), CalculateActivity::class.java)
                var calcItems = ArrayList<CalculateItem>()
                firebaseDB.dailyPicks.forEach({
                    var calcItem = CalculateItem(it.imgUrl,it.ticker,it.entryPrice,0f,0f,it.allocation)
                    calcItems.add(calcItem)
                })
                intent.putExtra("picks", calcItems)
                startActivity(intent)
            }
            Log.d("DailyWatchlistFragment","#Items: "+firebaseDB.dailyPicks.size)


//            var calculateDialog = CalculateDialogFragment()
//            calculateDialog.show(fragManager,"CalculateDialog")
        }

        val testObserv = Observable.just(1,2,3,4)
//        testObserv.subscribe{
//            Log.d("DailyWatchlistFragment",it.toString());
//        }
        testObserv.subscribeBy (
            onNext = {
                     Log.d("DailyWatchlistFragment","Looping through item: "+it.toString())
            },
            onComplete = {
                Log.d("DailyWatchlistFragment","Test Observation complete");
            }
        )

        /*
        Subscribe use case
         */
        val randObserables = Observable.range(1,10)
        val subscribtions = CompositeDisposable()
        val disposable = randObserables.subscribe {
            val n = it.toDouble()
            val fib = ((Math.pow(1.61803, n) - Math.pow(
                    0.61803,n
            )) / 2.23606).roundToInt()
            Log.d("DailyWatchlistFragment",fib.toString())
        }
        subscribtions.addAll(disposable)
        subscribtions.dispose()

        /*
        Create use case
         */
        val disposables = CompositeDisposable()
        Observable.create<String> {  emitter ->
            emitter.onNext("1")
            emitter.onError(RuntimeException("Error"))
            emitter.onComplete()
            emitter.onNext("?")
        }.subscribeBy (
            onNext = {
                Log.d("DailyWatchlistFragment",it.toString())
            },
        onComplete = {
            Log.d("DailyWatchlistFragment","Completed")
            },
                onError = {
                    Log.d("DailyWatchlistFragment",it.toString())
                }


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

        firebaseDB.setDailyDates(dailyDates,spinnerAdapter,pb_daily_picks,fab_calculate,dailyWatchRV)
        firebaseDB.pullUpdatedDailyPickTime(daily_stock_updatetime)
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