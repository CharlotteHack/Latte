package com.salad.latte.ClientManagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.salad.latte.ClientManagement.ViewModels.ActivityClientViewModel
import com.salad.latte.DailyWatchlistFragment
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.NewsFragment
import com.salad.latte.R
import com.salad.latte.Tutorial.TutorialActivity
import com.salad.latte.databinding.ActivityClientDashboardBinding
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

//import com.stripe.android.PaymentConfiguration
class ActivityClient : AppCompatActivity(){

    lateinit var viewPager :ViewPager2
    lateinit var tabLayout :TabLayout
    lateinit var adapter :ClientTabAdapter
    lateinit var viewModel :ActivityClientViewModel
    lateinit var binding :ActivityClientDashboardBinding
    val firebaseDB = FirebaseDB()

    override fun onStart() {
        super.onStart()
        if(!firebaseDB.isAuthenticated("ActivityClient")){
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ActivityClientViewModel()
        viewPager = findViewById(R.id.client_viewpager)
        tabLayout = findViewById(R.id.clientTabLayout)
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)


//        PaymentConfiguration.init(
//            applicationContext,
//            "pk_test_51LhElXHPhIXe6prQpqIO2xrJt6UFmTK05C0Jf1oR0xqj8x0jcYP9dbuNf416QaAbuUpUAHZGCxLQxQqFoF4mGq9X00eWHCQ8i7"
//        )


        lifecycleScope.launch {
            var didFinishTutorial = sharedPreference.getBoolean("isTutorialComplete",false)
            if(didFinishTutorial == false){
                var intent = Intent(this@ActivityClient,TutorialActivity::class.java)
                startActivity(intent)
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
                tab.text = "Trans."

            }
            else if(position == 2){
                tab.text = "Account"
            }
            else{
                tab.text = "News"
            }
        }.attach()

    }

    class ClientTabAdapter : FragmentStateAdapter {
        constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle){

        }



        override fun getItemCount(): Int {
            return 4;
        }

        override fun createFragment(position: Int): Fragment {
            Log.d("ActivityClient","Hit position: "+position)
            if(position == 0){
                return FragmentClientDashboard()
            }
            else if(position == 1){
                return FragmentClientTransactions()
            }
            else if (position == 2) {
                return FragmentClientSettings()
            }
            else  {
                return NewsFragment()
            }
        }


    }
}