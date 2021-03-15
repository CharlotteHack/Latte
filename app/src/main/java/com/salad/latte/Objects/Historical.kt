package com.salad.latte.Objects

import android.util.Log
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.salad.latte.R
import java.math.BigDecimal
import java.math.MathContext

class Historical(t :String,p :String, e :String, f :String, r :String, enP: String, exP :String, pr :String, a :String, divs :ArrayList<ArrayList<String>>) {


    var period = p
    var ticker = t
    var equity = e
    var fees = f
    var returns = r
    var entryPrice = enP
    var exitPrice = exP
    var percentReturn = pr
    var allocation = a
    var dividends = divs

    fun getExitDate() :String{
        return period.split(" ")[2]
    }

    fun getReturnPercent() :Double {
        return (exitPrice.toDouble()/entryPrice.toDouble())-1
    }
    fun getReturnPercentWithDividends() :Double {var sum = 0.0
        for (div in dividends){
            //3 represents the percent gained by each dividend date
            sum += div.get(3).toDouble()
        }
        return (exitPrice.toDouble()/entryPrice.toDouble())+sum-1
    }

    fun getDividendsPercentSum() : BigDecimal? {
        var sum = 0.0f
        for (div in dividends){
            //3 represents the percent gained by each dividend date
            sum += div.get(3).toFloat()
        }
        Log.d("Historical: ","Dividend  Sum Amount: "+sum)
        return (sum*100).toBigDecimal().round(MathContext(2));
    }



}

