package com.salad.latte.Adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.salad.latte.DashboardFragment
import com.salad.latte.HistoricalFragment
import com.salad.latte.MainFragment
import com.salad.latte.SettingsFragment

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
                HistoricalFragment()
            }
            2 -> {
                SettingsFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

}