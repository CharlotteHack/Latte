package com.salad.latte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.salad.latte.Adapters.WatchListAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Watchlist


class DashboardFragment : Fragment(){

    lateinit var tv_totaltrades :TextView
    lateinit var tv_winrate :TextView
    lateinit var tv_netreturn :TextView
    lateinit var tv_updateTime :TextView

//
    lateinit var gv_watchlist :GridView
    lateinit var watchitems :ArrayList<Watchlist>
    lateinit var watchAdapter :WatchListAdapter

    lateinit var firebaseDB :FirebaseDB
    lateinit var dashboard_progress :ProgressBar
    lateinit var tv_openPositions :TextView
    lateinit var tv_totalReturn :TextView

    lateinit var our_recentmoves_btn :Button
    lateinit var our_dividends :Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseDB = FirebaseDB();
        watchitems = ArrayList()
        val view = inflater.inflate(R.layout.fragment_dashboard,container,false)
//        tv_totaltrades = view.findViewById(R.id.tv_totaltrades2)
//        tv_winrate = view.findViewById(R.id.tv_winrate2)
//        tv_netreturn = view.findViewById(R.id.tv_netreturn2)

        our_recentmoves_btn = view.findViewById(R.id.our_recentmoves_btn);
        our_dividends = view.findViewById(R.id.our_dividends_btn);
        dashboard_progress = view.findViewById(R.id.progress_dashboard)
        gv_watchlist = view.findViewById(R.id.gv_watchlist)
        tv_updateTime = view.findViewById(R.id.timeUpdate)
        firebaseDB.pullUpdatedTime(tv_updateTime)
        tv_openPositions = view.findViewById(R.id.openPositions_tv)
        tv_totalReturn = view.findViewById(R.id.approxReturn_tv)
        watchitems.addAll(firebaseDB.pullWatchlistData(context!!,R.layout.custom_news_item,gv_watchlist,dashboard_progress,tv_openPositions, tv_totalReturn))
//        var watchlistItem = Watchlist();
//        watchlistItem.icon = "https://c0.klipartz.com/pngpicture/203/134/gratis-png-visa.png"
//        watchlistItem.ticker = "VISA (V)"
//        watchlistItem.allocation = "Allocation: 2%"
//        watchlistItem.targetEntry = "Target Entry: $100.71"
//        watchlistItem.buySellOrHold = "HOLD"
//        watchlistItem.entryDate = "12/31"
//        watchitems.add(Watchlist("https://e7.pngegg.com/pngimages/910/492/png-clipart-mastercard-logo-credit-card-visa-brand-mastercard-text-label.png","MA","Target Entry: $336.00",
//        "Allocation: 2%","HOLD","1/1"));




//        watchitems.add(watchlistItem)
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())
//        watchitems.add(Watchlist())//

        our_recentmoves_btn.setOnClickListener( View.OnClickListener {
            var intent = Intent(context!!,RecentMovesActivity::class.java);
            startActivity(intent)


        })
        our_dividends.setOnClickListener( View.OnClickListener {
            var intent = Intent(context!!,DividendsActivity::class.java);
            startActivity(intent)


        })

        Log.d("FirebaseDB2", "Results found for watchlist: " + watchitems.size)
        watchAdapter = WatchListAdapter(context!!,R.layout.custom_news_item,watchitems)
        gv_watchlist.setAdapter(watchAdapter)
        watchAdapter.notifyDataSetChanged()







        return view
    }



}
