package com.salad.latte

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Adapters.CalculateAdapter
import com.salad.latte.Objects.CalculateItem
import java.lang.Exception


class CalculateActivity : Activity() {

    lateinit var calculate_et :EditText
    lateinit var calculate_btn :Button
    lateinit var calculate_rv :RecyclerView
    lateinit var calculateAdapter :CalculateAdapter
    lateinit var calculateItems :ArrayList<CalculateItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_daily_investment)
//        Log.d("CalculateActivity", "Boop")

        if(intent.hasExtra("picks")){
//            var items = intent.getStringArrayExtra("items")
    try {
//        val items = intent.getParcelableArrayExtra("picks")
//        Log.d("CalculateActivity", "Picks found: " + items!!.size)
        calculate_btn = findViewById(R.id.calculate_button)
        calculate_rv = findViewById(R.id.calculate_list_rv)
        calculate_et = findViewById(R.id.calculate_invest_et)
        calculateItems = intent.getParcelableArrayExtra("picks") as ArrayList<CalculateItem>
                Log.d("CalculateActivity", "Picks found: " + calculateItems.size)

        calculateAdapter = CalculateAdapter(calculateItems,this)
        calculate_rv.adapter = calculateAdapter
        calculateAdapter.notifyDataSetChanged()
        calculate_btn.setOnClickListener {
            var investmentVal = calculate_et.text.toString()
            if(isNumber(investmentVal)){
                updateListWithNewCapital(investmentVal)
            }
            else{
                Toast.makeText(this,"Not a valid number",Toast.LENGTH_LONG).show()
            }
        }

    }
    catch (e : Exception){
        e.printStackTrace()
    }


        }

    }

    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    fun updateListWithNewCapital(investCap :String){
        calculateItems.forEach({
            it.totalCapital = investCap as Float
        })
        calculateAdapter.notifyDataSetChanged()
    }
}