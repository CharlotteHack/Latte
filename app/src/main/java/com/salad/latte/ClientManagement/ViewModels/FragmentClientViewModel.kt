package com.salad.latte.ClientManagement.ViewModels

import android.content.Intent
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
import com.salad.latte.ClientManagement.LoginActivity
import com.salad.latte.ClientManagement.SampleAsset
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Float

class FragmentClientViewModel(clientDashboard: FragmentClientDashboard) : ViewModel(){
    lateinit var firebaseDB : FirebaseDB
    lateinit var client : Client
    lateinit var convertIDToFirebase :String
    var dashboard = clientDashboard
    var assetsMutableStateFlow : MutableStateFlow<List<SampleAsset>> = MutableStateFlow(emptyList<SampleAsset>())
    var assetsStateFlow : StateFlow<List<SampleAsset>> = assetsMutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            firebaseDB = FirebaseDB()
            if(firebaseDB.auth.currentUser != null) {
                init()
                initAssets()
            }
            else{
                var intent = Intent(clientDashboard.context,LoginActivity::class.java)
                clientDashboard.startActivity(intent)
            }
        }
    }

    //Used to retrive the assets we are invested in
    suspend fun initAssets(){
        viewModelScope.launch {
            var emp_list = listOf(
                SampleAsset("https://getlogovector.com/wp-content/uploads/2020/03/proshares-logo-vector.png","TQQQ"),
                SampleAsset("https://p.kindpng.com/picc/s/127-1270696_bank-of-america-logo-icon-hd-png-download.png","BAC"),
                SampleAsset("https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,h_256,w_256,f_auto,q_auto:eco,dpr_1/yti8ctzowi1vnl6jzvab","SWK"),
                SampleAsset("https://www.freepnglogos.com/uploads/apple-logo-png/apple-logo-png-dallas-shootings-don-add-are-speech-zones-used-4.png","AAPL"),
                SampleAsset("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1200px-Microsoft_logo.svg.png","MSFT"),
                SampleAsset("https://www.freepnglogos.com/uploads/tesla-logo-png-25.png","TSLA"),
                SampleAsset("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Occidental-Petroleum-Logo.svg/1200px-Occidental-Petroleum-Logo.svg.png","OXY"),
                SampleAsset("https://s21.q4cdn.com/616071541/files/multimedia-gallery/assets/Logos/american-airlines/THUMB-aa_aa__vrt_rgb_grd_pos.png","AAL"),
                SampleAsset("https://www.freepnglogos.com/uploads/google-logo-png/google-logo-png-suite-everything-you-need-know-about-google-newest-0.png","GOOG")
            )
            assetsMutableStateFlow.value = emp_list
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
                tvAssetsWeinvestin.visibility = View.GONE
                rvAssets.visibility = View.GONE
                pbClientHome.visibility = View.VISIBLE
            }
        }
        else {
            dashboard.binding.apply {
                tvAccountValueHome.visibility = View.VISIBLE
                tvWelcomeHome.visibility = View.VISIBLE
                tvUrealizedPlHome.visibility = View.VISIBLE
                chartHome.visibility = View.VISIBLE
                tvAssetsWeinvestin.visibility = View.VISIBLE
                rvAssets.visibility = View.VISIBLE
                pbClientHome.visibility = View.GONE
            }
        }
    }

    suspend fun setValue(identifer :String){
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child(identifer).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            if(identifer.equals("name")) {
                client.client_name = it.value.toString()
                dashboard.binding.apply {
                    tvWelcomeHome.setText("Welcome, " + client.client_name)
                }
            }
            if(identifer.equals("accountValue"))
                client.client_balance = it.value.toString()
            dashboard.binding.apply {
                if("." in client.client_balance)
                    tvAccountValueHome.setText("Total Account Value: "+client.formatAccountValue())
            }
            if(identifer.equals("accountValueByDates")){
//                client.client_balance = it.value.toString()
                dashboard.binding.apply {
//                    tvAccountValueHome.setText("Total Account Value: "+client.client_balance)
                    Log.d("AccountValuesByDate: ", it.value.toString())
                    if (it.value != null)
                    {
                    var dataset = it.value as? HashMap<String, String>
                    client.clearClientValues()
                    dataset!!.forEach { (key, value) ->
                        var month = key.toString().split("-")[1]
                        var day = key.toString().split("-")[2]


                        Log.d("Fragment Client View Model client date", month + "-" + day)
                        client.addDateToValue(month + "-" + day, value)
                    }
//                    var chart = LineChart(dashboard.context)
//                    val chartTheme = ChartTheme.Builder().build()
                    Log.d("ClientValues: ", client.dateValues.size.toString())
                    val intervalList = client.dates
                    val rangeList = client.dateValues
                    val lineList = arrayListOf<Line>().apply {
                        add(Line("Total Account Value", Color.BLUE, client.dateValuesAsFloat))
//                        add(Line("Line 2", Color.RED, listOf(300f, 40f, 38f, 180f, 403f, 201f)))
                        chartHome.setOnClickListener {

                        }
                    }
                    chartHome.setData(lineList, intervalList, rangeList)
                    Log.d("AccountValuesByDate", dataset!!.get("2022-09-15").toString())
                } else {
                    Log.d("FragmentClientViewmodel","AccountValuesByDate is null")
                    }
                }
            }
            if(identifer.equals("unrealizedValue")) {
                client.client_unrealized_profit = it.value.toString()
                if(it.value != null) {
                Log.d("unrealizedValue: ",client.client_unrealized_profit)
                if(Float.valueOf(client.client_unrealized_profit) > 0f){
                    dashboard.binding.apply {
                        tvUrealizedPlHome.setText("Total Net Account Gain "+client.client_unrealized_profit)
                    }
                    displayProgress(false)

                }
                else {
                    dashboard.binding.apply {
                        tvUrealizedPlHome.setText("Total Net Account Loss: "+client.client_unrealized_profit)
                        displayProgress(false)

                    }
                }
            }
                else {
                    Log.d("FragmentClientVieModel","Unrealized profit is null")
                }

            }


        }.addOnFailureListener{
            Log.e("firebase", "Error getting account "+identifer+" -- email: "+convertIDToFirebase, it)
        if("Client is offline" in it.message.toString()){
//            Toast.makeText(dashboard.requireContext(),"Client is offline, trying again in 3 seconds",Toast.LENGTH_LONG).show()
            dashboard.binding.pbClientHome.visibility = View.INVISIBLE
            viewModelScope.launch {

            delay(10000)
            init()
            }
        }
        }
    }

}