package com.salad.latte.Objects

import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.salad.latte.R

class Historical() {

    var period = ""
    var equity = ""
    var fees = ""
    var returns = ""
    var equityPrevious = ""
    var equityCurrent = ""
    var percentReturn = ""
    constructor(period :String, equity :String, fees :String, returns :String, equityPrevious: String, equityCurrent :String, percentReturn :String){
        this.period = period
        this.equity = equity
        this.fees = fees
        this.returns = returns
        this.equityPrevious = equityPrevious
        this.equityCurrent = equityCurrent
        this.percentReturn = percentReturn

    }


}

