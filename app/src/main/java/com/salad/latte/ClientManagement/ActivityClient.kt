package com.salad.latte.ClientManagement

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

//import com.stripe.android.PaymentConfiguration
class ActivityClient : AppCompatActivity(){

    lateinit var viewPager :ViewPager2
    lateinit var tabLayout :TabLayout
    lateinit var adapter :ClientTabAdapter
    lateinit var firebaseDB : FirebaseDB
    final var stripeID = "U4693996"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_dashboard)
        viewPager = findViewById(R.id.client_viewpager)
        tabLayout = findViewById(R.id.clientTabLayout)
        firebaseDB = FirebaseDB()
//        PaymentConfiguration.init(
//            applicationContext,
//            "pk_test_51LhElXHPhIXe6prQpqIO2xrJt6UFmTK05C0Jf1oR0xqj8x0jcYP9dbuNf416QaAbuUpUAHZGCxLQxQqFoF4mGq9X00eWHCQ8i7"
//        )

        val url = URL("https://us-central1-latte-d25b7.cloudfunctions.net/createStripeCustomer?stripeid="+stripeID+"&firstname=Mohamed&lastname=Salad")
        val connection = url.openConnection()
        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
            var line: String?
            while (inp.readLine().also { line = it } != null) {
                println(line)
                Log.d("ActivityClient","createStripeCustomer: "+line)


            }
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {


            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        adapter = ClientTabAdapter(supportFragmentManager,lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if(position == 0){
                tab.text = "Home"
            }
            else if(position == 1){
                tab.text = "Transactions"

            }
            else{
                tab.text = "Account"

            }
        }.attach()

    }

    class ClientTabAdapter : FragmentStateAdapter {
        constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle){

        }



        override fun getItemCount(): Int {
            return 3;
        }

        override fun createFragment(position: Int): Fragment {
            Log.d("ActivityClient","Hit position: "+position)
            if(position == 0){
                return FragmentClientDashboard()
            }
            else if(position == 1){
                return FragmentClientTransactions()
            }
            else {
                return FragmentClientSettings()
            }
        }


    }
}