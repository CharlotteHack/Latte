package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.salad.latte.Adapters.DividendAdapter
import com.salad.latte.Adapters.WatchListAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Dialogs.WithdrawDialogFragment
import com.salad.latte.Objects.Dividend
import com.salad.latte.Objects.Settings
import com.salad.latte.Objects.Watchlist

class DividendsFragment :Fragment() {

    lateinit var lv_dividends : ListView
    lateinit var dividendsList :ArrayList<Dividend>
    lateinit var dividendAdapter : DividendAdapter

    lateinit var firebaseDB : FirebaseDB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dividends,container,false)
        dividendsList = ArrayList()
        firebaseDB = FirebaseDB();

        lv_dividends = view.findViewById(R.id.lv_dividends)
//        dividendsList.add(Dividend("-","2","2","3"))
        dividendsList.addAll(firebaseDB.pullDividendsData(context!!,R.layout.custom_dividends,lv_dividends))
        //After addAll isolate the stocks which we are in here
//        dividendAdapter = DividendAdapter(context!!,R.layout.custom_dividends,dividendsList)
//        lv_dividends.setAdapter(dividendAdapter)
//        dividendAdapter.notifyDataSetChanged()


        return view
    }



}