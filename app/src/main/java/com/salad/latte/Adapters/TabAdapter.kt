package com.salad.latte.Adapters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.database.FirebaseDatabase
import com.salad.latte.*
import com.salad.latte.Database.FirebaseDB

class TabAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DashboardFragment()
            }
            1 -> {
                var firebaseDB = FirebaseDB()
                var numAllocations = firebaseDB.pieCount
                var b = Bundle()
                b.putInt("allocationCount",numAllocations)
                PiechartFragment().setArguments(b)
                PiechartFragment()
            }
            2 -> {
                HistoricalFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

}