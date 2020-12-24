package com.salad.latte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class MainActivity : FragmentActivity() {

    lateinit var fragmentManager :FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager = supportFragmentManager;
        fragmentManager.beginTransaction().add(R.id.fragment,MainFragment()).commit()

    }
}