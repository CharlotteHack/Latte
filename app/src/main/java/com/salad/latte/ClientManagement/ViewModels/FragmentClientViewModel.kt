package com.salad.latte.ClientManagement.ViewModels

//import com.quipper.qandroidcomposechart.models.ChartTheme

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
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
                initAssets()
            } else {
                var intent = Intent(clientDashboard.context, LoginActivity::class.java)
                clientDashboard.startActivity(intent)
            }
        }
    }

    //Used to retrive the assets we are invested in
    suspend fun initAssets() {
        viewModelScope.launch {
            var emp_list = listOf(
                SampleAsset(
                    "https://getlogovector.com/wp-content/uploads/2020/03/proshares-logo-vector.png",
                    "TQQQ"
                ),
                SampleAsset(
                    "https://p.kindpng.com/picc/s/127-1270696_bank-of-america-logo-icon-hd-png-download.png",
                    "BAC"
                ),
                SampleAsset(
                    "https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,h_256,w_256,f_auto,q_auto:eco,dpr_1/yti8ctzowi1vnl6jzvab",
                    "SWK"
                ),
                SampleAsset(
                    "https://www.freepnglogos.com/uploads/apple-logo-png/apple-logo-png-dallas-shootings-don-add-are-speech-zones-used-4.png",
                    "AAPL"
                ),
                SampleAsset(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1200px-Microsoft_logo.svg.png",
                    "MSFT"
                ),
                SampleAsset("https://www.freepnglogos.com/uploads/tesla-logo-png-25.png", "TSLA"),
                SampleAsset(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Occidental-Petroleum-Logo.svg/1200px-Occidental-Petroleum-Logo.svg.png",
                    "OXY"
                ),
                SampleAsset(
                    "https://s21.q4cdn.com/616071541/files/multimedia-gallery/assets/Logos/american-airlines/THUMB-aa_aa__vrt_rgb_grd_pos.png",
                    "AAL"
                ),
                SampleAsset(
                    "https://www.freepnglogos.com/uploads/google-logo-png/google-logo-png-suite-everything-you-need-know-about-google-newest-0.png",
                    "GOOG"
                )
            )
            assetsMutableStateFlow.value = emp_list
        }

    }

    suspend fun init() {
        displayProgress(true)
        firebaseDB = FirebaseDB()
        var id = firebaseDB.auth.currentUser!!.email
        convertIDToFirebase = id!!.replace(".", "|");
        client = Client()
        setValue("name")
        setValue("accountValue")
        setValue("unrealizedValue")
        setValue("accountValueByDates")

        //Call setvalue for the account ID, if value is not none, display listview otherwise hide it
        setValue("accountID")
    }

    fun displayProgress(isOn: Boolean) {
        if (isOn) {
            dashboard.binding.apply {
                tvAccountValueHome.visibility = View.GONE
                tvWelcomeHome.visibility = View.GONE
                tvUrealizedPlHome.visibility = View.GONE
                chartHome.visibility = View.GONE
                tvAssetsWeinvestin.visibility = View.GONE
                rvAssets.visibility = View.GONE
                pbClientHome.visibility = View.VISIBLE
            }
        } else {
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

    suspend fun setValue(identifer: String) {
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child(identifer).get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                if (identifer.equals("name")) {
                    client.client_name = it.value.toString()
                    dashboard.binding.apply {
                        tvWelcomeHome.setText("Welcome, " + client.client_name)
                    }
                }
                if (identifer.equals("accountValue")) {
                    client.client_balance = it.value.toString()
                    dashboard.binding.apply {
                        if ("." in client.client_balance) {
                            tvAccountValueHome.setText("Total Account Value: " + client.formatAccountValue())
                        }
                    }
                }
                if (identifer.equals("accountValueByDates")) {
//                client.client_balance = it.value.toString()
                    dashboard.binding.apply {
//                    tvAccountValueHome.setText("Total Account Value: "+client.client_balance)
                        Log.d("AccountValuesByDate: ", it.value.toString())
                        if (it.value != null) {
                            var dataset = it.value as? HashMap<String, String>
                            client.clearClientValues()
                            dataset!!.forEach { (key, value) ->
                                var month = key.toString().split("-")[1]
                                var day = key.toString().split("-")[2]


                                Log.d("Fragment Client View Model client date", month + "-" + day)
                                client.addDateToValue(month + "-" + day, value)
                            }
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


//                        var chart = Li
//                    val chartTheme = ChartTheme.Builder().build()
                        Log.d("ClientValues: ", client.dateValues.size.toString())
//                    val intervalList = client.dates
//                    val rangeList = client.dateValues
                        val lineList = arrayListOf<Line>().apply {

                        }
                    }
                }
                if (identifer.equals("unrealizedValue")) {
                    client.client_unrealized_profit = it.value.toString()
                    if (it.value != null) {
                        Log.d("unrealizedValue: ", client.client_unrealized_profit)
                        if (Float.valueOf(client.client_unrealized_profit) >= 0f) {
                            dashboard.binding.apply {
                                tvUrealizedPlHome.setText("Total Net Account Gain " + client.client_unrealized_profit)
                            }
                            displayProgress(false)

                        } else {
                            dashboard.binding.apply {
                                tvUrealizedPlHome.setText("Total Net Account Loss: " + client.client_unrealized_profit)
                                displayProgress(false)

                            }
                        }
                    } else {
                        Log.d("FragmentClientVieModel", "Unrealized profit is null")
                    }

                }
                if (identifer.equals("accountID")) {
                    var accountID = it.value.toString()
                    dashboard.binding.apply {
                        if (accountID.equals("none")) {
                            tvAssetsWeinvestin.visibility = View.INVISIBLE
                            rvAssets.visibility = View.INVISIBLE
                        } else {
                            tvAssetsWeinvestin.visibility = View.VISIBLE
                            rvAssets.visibility = View.VISIBLE

                        }
                    }
                }
            }.addOnFailureListener {
                Log.e(
                    "firebase",
                    "Error getting account " + identifer + " -- email: " + convertIDToFirebase,
                    it
                )
                if ("Client is offline" in it.message.toString()) {
            Toast.makeText(dashboard.requireContext(),"Client is offline, trying again..",Toast.LENGTH_LONG).show()
                    dashboard.binding.pbClientHome.visibility = View.INVISIBLE
                    viewModelScope.launch {

                        delay(5000)
                        displayProgress(true)
                        init()
                        getIBKRAccount()
                    }
                }
            }

    }



    suspend fun getIBKRAccount(){
        var convertIDToFirebase = firebaseDB.auth.currentUser!!.email!!.replace(".","|");
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child("accountID").get().addOnSuccessListener {
            Log.i("ActivityClientViewModel", "Got value ${it.value}")
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

