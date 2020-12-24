package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsFragment :Fragment() {

    lateinit var tv_email :TextView
    lateinit var tv_join :TextView
    lateinit var tv_withdrawal_status :TextView
    lateinit var tv_deposit_status :TextView
    lateinit var btn_modify :Button
    lateinit var btn_withdraw :Button
    lateinit var btn_disclosure :Button
    lateinit var btn_privacy :Button
    lateinit var fab_instagram :FloatingActionButton
    lateinit var fab_email :FloatingActionButton
    lateinit var switch_trade :Switch
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings,container,false)
        tv_email = view.findViewById(R.id.tv_email)
        tv_join = view.findViewById(R.id.tv_email)
        tv_withdrawal_status = view.findViewById(R.id.tv_withdrawl_avail)
        tv_deposit_status = view.findViewById(R.id.tv_desposit_staus)
        btn_modify = view.findViewById(R.id.btn_modify)
        btn_withdraw = view.findViewById(R.id.btn_withdraw)
        btn_disclosure = view.findViewById(R.id.btn_disclosure)
        btn_privacy = view.findViewById(R.id.btn_privacy)

        return view
    }



}