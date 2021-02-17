package com.salad.latte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.transition.Visibility
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.salad.latte.Adapters.TabAdapter
import com.salad.latte.Dialogs.AddDialogFragment
import com.salad.latte.Dialogs.WithdrawDialogFragment

class MainActivity : FragmentActivity() {
    lateinit var fab_deposit :FloatingActionButton
    lateinit var fab_add_stock :FloatingActionButton
    lateinit var fragmentManager :FragmentManager
    lateinit var tb_tabbar :TabLayout
    lateinit var toolbar: Toolbar
    lateinit var title :TextView
    lateinit var iv_refresh :ImageView
    lateinit var viewPager :ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(getColor(R.color.purple_500))
        setContentView(R.layout.activity_main)
        fragmentManager = supportFragmentManager;
//        fragmentManager.beginTransaction().add(R.id.fragment,MainFragment()).commit()
        fab_deposit = findViewById(R.id.fab_deposit)
        fab_add_stock = findViewById(R.id.add_stock)
        tb_tabbar = findViewById(R.id.tabLayout2)
//        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.tv_title_label)
//        iv_refresh = findViewById(R.id.iv_refresh)
        viewPager = findViewById(R.id.viewpager)

//        Create TabLayout Adapter
        val tabLayout = TabAdapter(baseContext,fragmentManager,3)
        viewPager.adapter = tabLayout
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tb_tabbar))
        tb_tabbar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                if(tab.position > 0){
                    fab_deposit.hide()
                }
                else{
                    fab_deposit.show()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

//        iv_refresh.setOnClickListener(View.OnClickListener {
//            Toast.makeText(baseContext,"Clicked refresh",Toast.LENGTH_LONG).show()
//        })

        fab_deposit.setOnClickListener(View.OnClickListener {
            Toast.makeText(baseContext,"Clicked deposit",Toast.LENGTH_LONG).show()
        })
        fab_add_stock.setOnClickListener(View.OnClickListener {
            var addDialog = AddDialogFragment()
            addDialog.show(fragmentManager!!,"DIALOG_ADD")
        })
    }
}