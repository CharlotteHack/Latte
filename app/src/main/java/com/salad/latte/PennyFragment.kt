package com.salad.latte

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Adapters.PenniesAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.DailyWatchlistItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.util.HalfSerializer.onNext
import io.reactivex.rxjava3.kotlin.subscribeBy

class PennyFragment :Fragment() {

    lateinit var pennylist : RecyclerView
    lateinit var pennies :ArrayList<DailyWatchlistItem>
    lateinit var pennyObservable :Observable<Int>
    lateinit var pennyObserver :Disposable
    lateinit var adapter :PenniesAdapter
    var firebaseDB = FirebaseDB()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_penny, container, false)
        pennylist = v.findViewById(R.id.penny_list)
        var penny = DailyWatchlistItem("", "Test", 2.0f, 0.0f, 4.0f, 100.0f, "8/30/2022")
        pennies = ArrayList()
        pennies.add(penny)
        adapter = PenniesAdapter(context!!, R.layout.custom_daily_stock, pennies)
        pennylist.layoutManager = LinearLayoutManager(activity)
        pennylist.adapter = adapter
        adapter.notifyDataSetChanged()
        firebaseDB.pullPennyPicks(this)
        Log.d("PennyFragment","penny")
        pennyObservable = Observable.create { emitter ->
            //Make firebase call here
            emitter.onNext(1)

        }



        return v;
    }


    fun updateObserver(){
        pennyObserver = pennyObservable.subscribeBy (
                onNext = {
                    adapter.notifyDataSetChanged()
                    Log.d("PennyFragment","Called Observer")
                }
        )
    }


}