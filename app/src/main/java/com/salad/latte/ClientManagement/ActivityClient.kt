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
import com.salad.latte.R

class ActivityClient : AppCompatActivity(){

    lateinit var viewPager :ViewPager2
    lateinit var tabLayout :TabLayout
    lateinit var adapter :ClientTabAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_dashboard)
        viewPager = findViewById(R.id.client_viewpager)
        tabLayout = findViewById(R.id.clientTabLayout)
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
                return FragmentClientDashboard()
            }
        }


    }
}