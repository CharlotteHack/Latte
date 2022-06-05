package com.salad.latte.Objects

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import java.util.*

class User : Observer{
    var name = "Mohamed"
    init {

    }


    override fun update(o: java.util.Observable?, arg: Any?) {
        Log.d("DailyWatchlistFragment","User is an observer")
    }

    fun writeName(){
        Log.d("DailyWatchlistFragment","User name: "+name)
    }
}