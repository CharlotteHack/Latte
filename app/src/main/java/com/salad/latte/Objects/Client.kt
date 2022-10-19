package com.salad.latte.Objects

class Client {
    var client_account_number = ""
     var client_name = ""
     var client_realized_profit = ""
     var client_unrealized_profit = ""
     var client_email = ""
     var client_balance = ""
    var clientValueByDate = HashMap<String,String>()
    var dates = ArrayList<String>()
    var dateValues = ArrayList<String>()
    var dateValuesAsFloat = ArrayList<Float>()

    init {

    }
    fun Float.format(digits: Int) = "%.${digits}f".format(this)

    fun formatAccountValue() : String {
        var av = client_balance.toFloat()
        return av.format(2).toString()
    }
    fun clearClientValues(){
        clientValueByDate = HashMap<String,String>()
        dateValues.clear()
        dateValuesAsFloat.clear()
        dates.clear()
    }

    fun addDateToValue(date :String, value :String){
        clientValueByDate.put(date,value)
        dates.add(date)
        dateValues.add(value)
        dateValuesAsFloat.add(value.toFloat())
    }
}