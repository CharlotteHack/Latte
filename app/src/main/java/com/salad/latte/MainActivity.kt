package com.salad.latte

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.salad.latte.Adapters.TabAdapter
import com.salad.latte.Database.CustomFirebaseMessagingService
import com.salad.latte.Dialogs.AddDialogFragment
import com.salad.latte.Dialogs.CalculateDialogFragment
import com.salad.latte.Tutorial.TutorialActivity
import es.dmoral.toasty.Toasty

class MainActivity : FragmentActivity() {
    lateinit var fab_deposit :FloatingActionButton
    lateinit var fab_add_stock :FloatingActionButton
    lateinit var fragmentManager :FragmentManager
    lateinit var tb_tabbar :TabLayout
    lateinit var toolbar: Toolbar
    lateinit var title :TextView
    lateinit var iv_instagram :ImageView
    lateinit var iv_question :ImageView
    lateinit var iv_feedback :ImageView
    lateinit var iv_pie :ImageView
    lateinit var viewPager :ViewPager
    lateinit var iv_history :ImageView
    val PREFS_FILENAME = "com.tutorial"
    var prefs: SharedPreferences? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var customFBService = CustomFirebaseMessagingService()
        customFBService.getToken()
        window.setStatusBarColor(getColor(R.color.purple_500))
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        if(prefs!!.getBoolean("didViewTutorial",false) == false){
            val i = Intent(this, TutorialActivity::class.java)
            startActivity(i)
        }
        Toasty.info(this, "Not Investment Advice. Do Due Diligence.", Toast.LENGTH_SHORT, true).show();
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
        iv_instagram = findViewById(R.id.iv_instagram)
        iv_question = findViewById(R.id.iv_question)
        iv_feedback = findViewById(R.id.iv_feedback)
        iv_pie = findViewById(R.id.iv_pie)
        iv_history = findViewById(R.id.history_iv)

        iv_instagram.setOnClickListener{
            var uri = Uri.parse("http://instagram.com/_u/dollarcostavg");
            var insta = Intent(Intent.ACTION_VIEW, uri);
            insta.setPackage("com.instagram.android");

            if (isIntentAvailable(baseContext, insta)){
                startActivity(insta);
            } else{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/dollarcostavg")));
            }



        }

        iv_question.setOnClickListener(View.OnClickListener {
            val i = Intent(this, TutorialActivity::class.java)
            startActivity(i)
        })

        iv_history.setOnClickListener(View.OnClickListener {
            val i = Intent(this, HistoryActivity::class.java)
            startActivity(i)
        })

        iv_pie.setOnClickListener(View.OnClickListener {
            val i = Intent(this, PieActivity::class.java)
            startActivity(i)
        })

//        Create TabLayout Adapter
        val tabLayout = TabAdapter(baseContext,fragmentManager,3)
        viewPager.adapter = tabLayout
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tb_tabbar))
        tb_tabbar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
//                if(tab.position > 0){
//                    fab_deposit.hide()
//                }
//                else{
//                    fab_deposit.show()
//                }///////////
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

    fun isIntentAvailable(ctx :Context, intent :Intent) : Boolean{
        val packageManager = ctx.packageManager
        var list = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }

}