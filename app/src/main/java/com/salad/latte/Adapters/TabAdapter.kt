package com.salad.latte.Adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.salad.latte.*

class TabAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DailyWatchlistFragment()
            }
//            1 -> {
//               RecentFragment()
//            }
            1 -> {
                DashboardFragment()
            }
            2 -> {
                SuperInvestorsFragment()
            }
//            3 -> {
//                DividendsFragment()
//            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

}