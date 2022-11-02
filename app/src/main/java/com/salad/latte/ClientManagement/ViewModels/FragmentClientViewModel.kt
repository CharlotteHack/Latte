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
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
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
                    var chartArray = mutableListOf<Double>()
                    var chartDates = mutableListOf<String>()
//                client.client_balance = it.value.toString()
                    dashboard.binding.apply {
//                    tvAccountValueHome.setText("Total Account Value: "+client.client_balance)
//                        var accountValues = snapshot.getValue(HashMap::class.java)
                        client.clearClientValues()
                        if(snapshot.childrenCount.toInt() == 0){
                            //No chart items
                            pbClientHome.visibility = View.INVISIBLE
                            chartHome.visibility = View.INVISIBLE
                            nochartView.visibility = View.VISIBLE
                        }
                        else {

                            pbClientHome.visibility = View.INVISIBLE
                            chartHome.visibility = View.VISIBLE
                            nochartView.visibility = View.INVISIBLE
                            for (ds in snapshot.children) {
//                            Log.d("AccountValuesByDate: ", accountValues.toString())
                                var month = ds.key.toString().split("-")[1]
                                var day = ds.key.toString().split("-")[2]


                                Log.d(
                                    "FragmentClientViewModel client date",
                                    month + "-" + day
                                )
                                client.addDateToValue(
                                    month + "-" + day,
                                    ds.getValue(String::class.java)!!
                                )
                                Log.d(
                                    "FragmentClientViewModel client value",
                                    ds.getValue(String::class.java)!!)
                                var vall = ds.getValue(String::class.java)!!
                                var cl = Client()
                                cl.client_balance = vall

                                chartArray.add(java.lang.Double(cl.formatAccountValue()).toDouble())
                                chartDates.add(ds.key!!)


                            }
                            Log.d(
                                "FragmentClientViewModel Chart Size:",
                                chartArray.size.toString())

                            //Time to plot on chart
                            val aaChartModel : AAChartModel = AAChartModel()
                                .chartType(AAChartType.Line)
                                .title("Account Value By Month")
                                .backgroundColor("#FFFFFF")
                                .dataLabelsEnabled(true)
                                .series(arrayOf(
                                    AASeriesElement()
                                        .name("Account")
                                        .data(chartArray.toTypedArray())
                                ))

                                .categories(
                                    chartDates.toTypedArray()
                                )
                            dashboard.binding.chartHome.aa_drawChartWithChartModel(aaChartModel)
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

