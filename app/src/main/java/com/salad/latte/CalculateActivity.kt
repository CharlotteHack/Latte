package com.salad.latte

import android.app.Activity
import android.os.Bundle
import android.util.Log


class CalculateActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_calculate_daily_investment_activity)
        Log.d("CalculateActivity", "Boop")

        if(intent.hasExtra("picks")){
//            var items = intent.getStringArrayExtra("items")
            val args = intent.getBundleExtra("picks")
            val items = args!!.getSerializable("picks") as ArrayList<Any>?


            Log.d("CalculateActivity", "Picks found: " + items!!.size)
        }
    }
}