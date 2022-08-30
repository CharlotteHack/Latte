package com.salad.latte.Adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.salad.latte.*
import com.salad.latte.DeprecatedClasses.HistoricalFragment
import com.salad.latte.DeprecatedClasses.RecentFragment
import com.salad.latte.Tutorial.CorporateInsidersFragment

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
//                DailyHistoricalFragment()
//            }
            1 -> {
                PennyFragment()
            }
            2 -> {
               NewsFragment()
            }
//            2 -> {
//                RecentFragment()
//            }
//            1 -> {
//               RecentFragment()
//            }
//            1 -> {
//                DashboardFragment()
//            }
//            1 -> {
//                SuperInvestorsFragment()
//            }
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