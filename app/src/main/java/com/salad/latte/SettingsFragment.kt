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
import com.salad.latte.Dialogs.WithdrawDialogFragment
import com.salad.latte.Objects.Settings

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
        val setting = Settings("Email: latteapp@gmail.io","Join Date: October 13 2020","Available for Withdrawl: $1,000.00","Deposit Status: Not Scheduled")
        tv_email = view.findViewById(R.id.tv_email)
        tv_email.setText(setting.email)

        tv_join = view.findViewById(R.id.tv_email)
        tv_join.setText(setting.joinDate)

        tv_withdrawal_status = view.findViewById(R.id.tv_withdrawl_avail)
        tv_withdrawal_status.setText(setting.withdralStatus)

        tv_deposit_status = view.findViewById(R.id.tv_desposit_staus)
        tv_deposit_status.setText(setting.depositStatus)

        btn_modify = view.findViewById(R.id.btn_modify)
        btn_modify.setOnClickListener(View.OnClickListener {

        })
        btn_withdraw = view.findViewById(R.id.btn_withdraw)
        btn_withdraw.setOnClickListener(View.OnClickListener {
            var withdrawDialog = WithdrawDialogFragment()
            withdrawDialog.show(fragmentManager!!,"DIALOG_WITHDRAW")

        })
        btn_disclosure = view.findViewById(R.id.btn_disclosure)
        btn_disclosure.setOnClickListener(View.OnClickListener {

        })
        btn_privacy = view.findViewById(R.id.btn_privacy)
        btn_privacy.setOnClickListener(View.OnClickListener {

        })

        fab_email = view.findViewById(R.id.fab_email)
        fab_email.setOnClickListener(View.OnClickListener {

        })

        fab_instagram = view.findViewById(R.id.fab_instagram)
        fab_instagram.setOnClickListener(View.OnClickListener {

        })

        switch_trade = view.findViewById(R.id.switch_trade)
        switch_trade.setOnClickListener(View.OnClickListener {

        })

        return view
    }



}