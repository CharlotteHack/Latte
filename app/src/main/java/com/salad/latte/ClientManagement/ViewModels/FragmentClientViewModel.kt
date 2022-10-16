package com.salad.latte.ClientManagement.ViewModels

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.LineChart
import com.naqdi.chart.model.Line
//import com.quipper.qandroidcomposechart.models.ChartTheme
import com.salad.latte.ClientManagement.FragmentClientDashboard
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Float

class FragmentClientViewModel(clientDashboard: FragmentClientDashboard) : ViewModel(){
    lateinit var firebaseDB : FirebaseDB
        lateinit var client : Client
    lateinit var convertIDToFirebase :String
    var dashboard = clientDashboard

    init {
        viewModelScope.launch {
            init()
        }
    }

    suspend fun init(){
        displayProgress(true)
        firebaseDB = FirebaseDB()
        var id = firebaseDB.auth.currentUser!!.email
        convertIDToFirebase = id!!.replace(".","|");
        client = Client()
        setValue("name")
        setValue("accountValue")
        setValue("unrealizedValue")
        setValue("accountValueByDates")
    }
    fun displayProgress(isOn :Boolean){
        if(isOn){
            dashboard.binding.apply {
                tvAccountValueHome.visibility = View.GONE
                tvWelcomeHome.visibility = View.GONE
                tvUrealizedPlHome.visibility = View.GONE
                chartHome.visibility = View.GONE
                pbClientHome.visibility = View.VISIBLE
            }
        }
        else {
            dashboard.binding.apply {
                tvAccountValueHome.visibility = View.VISIBLE
                tvWelcomeHome.visibility = View.VISIBLE
                tvUrealizedPlHome.visibility = View.VISIBLE
                chartHome.visibility = View.VISIBLE
                pbClientHome.visibility = View.GONE
            }
        }
    }

    suspend fun setValue(identifer :String){
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child(identifer).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            if(identifer.equals("name"))
                client.client_name = it.value.toString()
                dashboard.binding.apply {
                    tvWelcomeHome.setText("Welcome, "+client.client_name)
                }
            if(identifer.equals("accountValue"))
                client.client_balance = it.value.toString()
            dashboard.binding.apply {
                tvAccountValueHome.setText("Total Account Value: "+client.client_balance)
            }
            if(identifer.equals("accountValueByDates")){
//                client.client_balance = it.value.toString()
                dashboard.binding.apply {
//                    tvAccountValueHome.setText("Total Account Value: "+client.client_balance)
                    Log.d("AccountValuesByDate: ",it.value.toString())
                    var dataset = it.value as? HashMap<String,String>
                    client.clearClientValues()
                    dataset!!.forEach { (key, value) ->
                        println("$key = $value")
                        client.addDateToValue(key,value)
                    }
//                    var chart = LineChart(dashboard.context)
//                    val chartTheme = ChartTheme.Builder().build()
                    Log.d("ClientValues: ",client.dateValues.size.toString())
                    val intervalList = client.dates
                    val rangeList = client.dateValues
                    val lineList = arrayListOf<Line>().apply {
                        add(Line("Total Account Value", Color.BLUE, client.dateValuesAsFloat))
//                        add(Line("Line 2", Color.RED, listOf(300f, 40f, 38f, 180f, 403f, 201f)))
                        chartHome.setOnClickListener {

                    }
                    }
                    chartHome.setData(lineList, intervalList, rangeList)


                    Log.d("AccountValuesByDate",dataset!!.get("2022-09-15").toString())
                }
            }
            if(identifer.equals("unrealizedValue")) {
                client.client_unrealized_profit = it.value.toString()
                Log.d("unrealizedValue: ",client.client_unrealized_profit)
                if(Float.valueOf(client.client_unrealized_profit) > 0f){
                    dashboard.binding.apply {
                        tvUrealizedPlHome.setText("Total Net Account Gain "+client.client_unrealized_profit)
                    }
                    displayProgress(false)

                }

            }
            else {
                dashboard.binding.apply {
                    tvUrealizedPlHome.setText("Total Net Account Loss: "+client.client_unrealized_profit)
                    displayProgress(false)

                }
            }


        }.addOnFailureListener{
            Log.e("firebase", "Error getting account "+identifer+" -- email: "+convertIDToFirebase, it)
        if("Client is offline" in it.message.toString()){
            Toast.makeText(dashboard.requireContext(),"Client is offline, trying again in 3 seconds",Toast.LENGTH_LONG).show()
            dashboard.binding.pbClientHome.visibility = View.INVISIBLE
            viewModelScope.launch {

            delay(3000)
            init()
            }
        }
        }
    }

}