package com.salad.latte

import android.app.Activity
import android.os.Bundle
import android.util.Log
import java.lang.Exception


class CalculateActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_calculate_daily_investment_activity)
        Log.d("CalculateActivity", "Boop")

        if(intent.hasExtra("picks")){
//            var items = intent.getStringArrayExtra("items")
    try {
        val items = intent.getParcelableArrayExtra("picks")
        Log.d("CalculateActivity", "Picks found: " + items!!.size)

    }
    catch (e : Exception){
        e.printStackTrace()
    }


        }

    }
}