package com.salad.latte.Objects

import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.salad.latte.R

class Historical(t :String,p :String, e :String, f :String, r :String, enP: String, exP :String, pr :String, a :String) {


    var period = p
    var ticker = t
    var equity = e
    var fees = f
    var returns = r
    var entryPrice = enP
    var exitPrice = exP
    var percentReturn = pr
    var allocation = a

    fun getExitDate() :String{
        return period.split(" ")[2]
    }

    fun getReturnPercent() :Double {
        return (exitPrice.toDouble()/entryPrice.toDouble())-1
    }



}

