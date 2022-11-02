package com.salad.latte.ClientManagement.ViewModels

//import com.quipper.qandroidcomposechart.models.ChartTheme

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.naqdi.chart.model.Line
import com.salad.latte.ClientManagement.FragmentClientDashboard
import com.salad.latte.ClientManagement.LoginActivity
import com.salad.latte.ClientManagement.SampleAsset
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Float
import java.text.DecimalFormat
import kotlin.Boolean
import kotlin.String
import kotlin.apply
import kotlin.toString


class FragmentClientViewModel(clientDashboard: FragmentClientDashboard) : ViewModel() {
    lateinit var firebaseDB: FirebaseDB
    lateinit var client: Client
    lateinit var convertIDToFirebase: String
    var dashboard = clientDashboard
    var assetsMutableStateFlow: MutableStateFlow<List<SampleAsset>> =
        MutableStateFlow(emptyList<SampleAsset>())
    var assetsStateFlow: StateFlow<List<SampleAsset>> = assetsMutableStateFlow.asStateFlow()
    var ibkrClientIDMutableStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    var ibkrClientIDStateFlow = ibkrClientIDMutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            firebaseDB = FirebaseDB()
            if (firebaseDB.auth.currentUser != null) {
                init()
//                initAssets()
            } else {
                var intent = Intent(clientDashboard.context, LoginActivity::class.java)
                clientDashboard.startActivity(intent)
            }
        }
    }

    //Used to retrive the assets we are invested in
//    suspend fun initAssets() {
//        viewModelScope.launch {
//            var emp_list = listOf(
//                SampleAsset(
//                    "https://getlogovector.com/wp-content/uploads/2020/03/proshares-logo-vector.png",
//                    "TQQQ",
//                    date = "10/12/2025",
//                    timestamp = 100000
//                )
//            )
//
//        }
//
//    }

    suspend fun init() {
        Log.d("(82) FragmentClientViewModel","Init called")

        //ON init, PB needs to be on,
        //Other views all off
        dashboard.binding.apply {
            pbClientHome.visibility = View.VISIBLE
            chartHome.visibility = View.INVISIBLE
            nochartView.visibility = View.INVISIBLE
            nosavingsView.visibility = View.INVISIBLE
        }

        var id = firebaseDB.auth.currentUser!!.email
        convertIDToFirebase = id!!.replace(".", "|");
        client = Client()
        setValue("name")
        setValue("accountValue")
//        setValue("unrealizedValue")
        setValue("accountValueByDates")
//        setValue("savings")

        //Call setvalue for the account ID, if value is not none, display listview otherwise hide it
        setValue("accountID")
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }
    suspend fun setValue(identifer: String) {
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child(identifer).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.i("firebase", "Got value ${it.value}")
                if (identifer.equals("name")) {
                    client.client_name = snapshot.getValue(String::class.java).toString()
                    dashboard.binding.apply {
                        Log.d(" (165) FragmentClientViewModel","Name: "+client.client_name)

                        tvWelcomeHome.setText("Welcome, " + client.client_name)
                    }
                }
                if (identifer.equals("accountValue")) {
                    client.client_balance = snapshot.getValue(String::class.java).toString()
                    Log.d(" (172) FragmentClientViewModel","Snapshot: "+snapshot.toString())
                    Log.d(" (173) FragmentClientViewModel","Account Value: "+client.client_balance)

                    dashboard.binding.apply {
//                        if ("." in client.client_balance) {
                        tvAccountValueHome.setText("Account Value: $" + client.currencyFormat(client.client_balance))
//                        }
                    }
                }
                if (identifer.equals("savings")) {
                    var totalSavings = 0.0
                    var savingsList = mutableListOf<SampleAsset>()
                    if(snapshot.childrenCount.toInt() == 0){
                        dashboard.binding.apply {
                            nosavingsView.visibility = View.VISIBLE
                            rvAssets.visibility = View.INVISIBLE
                            tvUrealizedPlHome.text = "Savings earned: $0.00";
                            tvAssetsWeinvestin.text = "Monthly Savings Payouts"
                        }
                    }
                    else {
                        for (ds in snapshot.children) {
//                    var saving = SampleAsset()
                                    var amount = ds.child("amount").getValue(Double::class.java)!!
                                    var date = ds.child("date").getValue(String::class.java)!!
                                    var ts = ds.child("timestamp").getValue(Long::class.java)!!
                                    totalSavings = totalSavings + amount
                                    Log.d(" (147) FragmentClientViewModel", "Amount: " + amount)
                                    savingsList.add(SampleAsset("", amount, date, ts))
                                assetsMutableStateFlow.value = savingsList
                        }
                        dashboard.binding.apply {
                            nosavingsView.visibility = View.INVISIBLE
                            rvAssets.visibility = View.VISIBLE
                            tvUrealizedPlHome.text = "Savings earned: $"+currencyFormat(totalSavings.toString())
                        }
                        dashboard.binding.apply {
                            tvAssetsWeinvestin.text = "Monthly Savings Payouts"
                        }
                    }

                }
                if (identifer.equals("accountValueByDates")) {
//                client.client_balance = it.value.toString()
                    dashboard.binding.apply {
//                    tvAccountValueHome.setText("Total Account Value: "+client.client_balance)
//                        var accountValues = snapshot.getValue(HashMap::class.java)
                        client.clearClientValues()
                        if(snapshot.childrenCount.toInt() > 0){
                            //No chart items
                            pbClientHome.visibility = View.INVISIBLE
                            chartHome.visibility = View.VISIBLE
                            nochartView.visibility = View.INVISIBLE
                        }
                        else {

                            pbClientHome.visibility = View.INVISIBLE
                            chartHome.visibility = View.INVISIBLE
                            nochartView.visibility = View.VISIBLE
                            for (ds in snapshot.children) {
//                            Log.d("AccountValuesByDate: ", accountValues.toString())
                                var month = ds.key.toString().split("-")[1]
                                var day = ds.key.toString().split("-")[2]


                                Log.d(
                                    "Fragment Client View Model client date",
                                    month + "-" + day
                                )
                                client.addDateToValue(
                                    month + "-" + day,
                                    ds.getValue(String::class.java)!!
                                )
                                var chart = AnyChartView(dashboard.context)
                                var cartesian = AnyChart.line()
                                cartesian.animation(true)

                                cartesian.padding(10.0, 20.0, 5.0, 20.0)

                                cartesian.crosshair().enabled(true)
                                cartesian.crosshair()
                                    .yLabel(true) // TODO ystroke
                                    .yStroke()

                                cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

                                cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.")

                                cartesian.yAxis(0).title("Number of Bottles Sold (thousands)")
                                cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

                                val seriesData: MutableList<DataEntry> = ArrayList()
                                seriesData.add(CustomDataEntry("1986", 3.6, 2.3, 2.8))
                                seriesData.add(CustomDataEntry("1987", 7.1, 4.0, 4.1))
                                seriesData.add(CustomDataEntry("1988", 8.5, 6.2, 5.1))
                                seriesData.add(CustomDataEntry("1989", 9.2, 11.8, 6.5))
                                seriesData.add(CustomDataEntry("1990", 10.1, 13.0, 12.5))
                                seriesData.add(CustomDataEntry("1991", 11.6, 13.9, 18.0))
                                seriesData.add(CustomDataEntry("1992", 16.4, 18.0, 21.0))
                                seriesData.add(CustomDataEntry("1993", 18.0, 23.3, 20.3))
                                seriesData.add(CustomDataEntry("1994", 13.2, 24.7, 19.2))
                                seriesData.add(CustomDataEntry("1995", 12.0, 18.0, 14.4))
                                seriesData.add(CustomDataEntry("1996", 3.2, 15.1, 9.2))
                                seriesData.add(CustomDataEntry("1997", 4.1, 11.3, 5.9))
                                seriesData.add(CustomDataEntry("1998", 6.3, 14.2, 5.2))
                                seriesData.add(CustomDataEntry("1999", 9.4, 13.7, 4.7))
                                seriesData.add(CustomDataEntry("2000", 11.5, 9.9, 4.2))
                                seriesData.add(CustomDataEntry("2001", 13.5, 12.1, 1.2))
                                seriesData.add(CustomDataEntry("2002", 14.8, 13.5, 5.4))
                                seriesData.add(CustomDataEntry("2003", 16.6, 15.1, 6.3))
                                seriesData.add(CustomDataEntry("2004", 18.1, 17.9, 8.9))
                                seriesData.add(CustomDataEntry("2005", 17.0, 18.9, 10.1))
                                seriesData.add(CustomDataEntry("2006", 16.6, 20.3, 11.5))
                                seriesData.add(CustomDataEntry("2007", 14.1, 20.7, 12.2))
                                seriesData.add(CustomDataEntry("2008", 15.7, 21.6, 10))
                                seriesData.add(CustomDataEntry("2009", 12.0, 22.5, 8.9))

                                var set = com.anychart.data.Set.instantiate()
                                set.data(seriesData);
                                var series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

                                var series1 = cartesian.line(series1Mapping);
                                series1.name("Brandy");
                                series1.hovered().markers().enabled(true);
                                series1.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4);
                                series1.tooltip()
                                    .position("right")
                                    .anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5)
                                    .offsetY(5);


                                cartesian.legend().enabled(true);
                                cartesian.legend().fontSize(13);
                                cartesian.legend().padding(0, 0, 10, 0);

                                chart.setChart(cartesian);

                            }
                        }


//                        var chart = Li
//                    val chartTheme = ChartTheme.Builder().build()
                        Log.d("ClientValues: ", client.dateValues.size.toString())
//                    val intervalList = client.dates
//                    val rangeList = client.dateValues
                        val lineList = arrayListOf<Line>().apply {

                        }
                    }
                }
//                if (identifer.equals("unrealizedValue")) {
//                    client.client_unrealized_profit = it.value.toString()
//                    if (it.value != null) {
//                        Log.d("unrealizedValue: ", client.client_unrealized_profit)
//
//                        dashboard.binding.apply {
//                            tvUrealizedPlHome.setText("Savings earned $" + client.currencyFormat(client.client_unrealized_profit))
//                        }
//                        displayProgress(false)
//                    } else {
//                        Log.d("FragmentClientVieModel", "Unrealized profit is null")
//                    }
//
//                }
                if (identifer.equals("accountID")) {
                    var accountID = snapshot.getValue(String::class.java).toString()
                    dashboard.binding.apply {
                        Log.d("FragmentClientViewModel", "Account ID: "+accountID)
                        if (accountID.equals("none")) {
//                            tvAssetsWeinvestin.visibility = View.INVISIBLE
//                            rvAssets.visibility = View.INVISIBLE
                            ibkrClientIDMutableStateFlow.value = "none"
                            tvAssetsWeinvestin.text = ""
                            nochartView.visibility = View.VISIBLE
                            chartHome.visibility = View.INVISIBLE
                            tvUrealizedPlHome.text = "Savings earned: $0.00";

                        } else {
//                            tvAssetsWeinvestin.visibility = View.VISIBLE
//                            rvAssets.visibility = View.VISIBLE
                            ibkrClientIDMutableStateFlow.value = accountID
                            viewModelScope.launch {
                                setValue("savings")
                            }

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {

                        delay(3000)
                        Toast.makeText(dashboard.requireContext(),error.message,Toast.LENGTH_LONG).show()
                        Toast.makeText(dashboard.requireContext(),"Error, trying again.",Toast.LENGTH_SHORT).show()
                        setValue(identifer)

                    }
            }


        })

    }



    suspend fun getIBKRAccount(){
        var convertIDToFirebase = firebaseDB.auth.currentUser!!.email!!.replace(".","|");
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child("accountID").get().addOnSuccessListener {
            Log.i("FragmentClientViewModel", "Got value ${it.value}")
            ibkrClientIDMutableStateFlow.value = it.getValue(String::class.java)!!;
        }
    }

    private class CustomDataEntry(x :String, value_ :Number, value2 :Number, value3 :Number) : ValueDataEntry(x, value_) {

        init {
            super.setValue("value2", value2);
            super.setValue("value3", value3);
        }

    }

}

