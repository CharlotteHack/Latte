package com.salad.latte

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.Adapters.CalculateAdapter
import com.salad.latte.Objects.CalculateItem
import java.lang.Exception


class CalculateActivity : Activity() {

    lateinit var calculate_et: EditText
    lateinit var calculate_btn: Button
    lateinit var calculate_rv: RecyclerView
    lateinit var calculateAdapter: CalculateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_daily_investment)
        var calculateItems: ArrayList<CalculateItem>?

//        Log.d("CalculateActivity", "Boop")

        if (intent.hasExtra("picks")) {
//            var items = intent.getStringArrayExtra("items")
            try {
//        val items = intent.getParcelableArrayExtra("picks")
//        Log.d("CalculateActivity", "Picks found: " + items!!.size)
                calculate_btn = findViewById(R.id.calculate_button)
                calculate_rv = findViewById(R.id.calculate_list_rv)
                calculate_et = findViewById(R.id.calculate_invest_et)
                calculateItems = intent.getParcelableArrayListExtra<CalculateItem>("picks") as? ArrayList<CalculateItem>
                Log.d("CalculateActivity", "Picks found: " + calculateItems!!.size)

                calculateAdapter = CalculateAdapter(calculateItems, this)
                calculate_rv.layoutManager = LinearLayoutManager(this)
                calculate_rv.adapter = calculateAdapter
                calculateAdapter.notifyDataSetChanged()

                calculate_btn.setOnClickListener {
                    var investmentVal = calculate_et.text.toString()
                    Log.d("CalculateActivity","EditText value: "+investmentVal)
                    if (isNumber(investmentVal)) {
                        updateListWithNewCapital(calculateItems,investmentVal.toFloat())
                        Toast.makeText(this, "Updated the shares to buy column",Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Not a valid number", Toast.LENGTH_LONG).show()
                    }
                    hideKeyboard()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

    }

    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    fun updateListWithNewCapital(cap :ArrayList<CalculateItem>,investCap: Float) {
        cap.forEach({
            it.totalCapital = investCap
        })
        calculateAdapter.notifyDataSetChanged()
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    }

}