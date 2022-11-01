package com.salad.latte.ClientManagement

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.salad.latte.R

class ActivityWithdrawalSuccessful : AppCompatActivity() {

    lateinit var successBtn : Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_success)
        successBtn = findViewById<Button>(R.id.deposit_success_btn)
        successBtn.setOnClickListener {
            var intent = Intent(this,ActivityClient::class.java)
            startActivity(intent)
        }
    }}