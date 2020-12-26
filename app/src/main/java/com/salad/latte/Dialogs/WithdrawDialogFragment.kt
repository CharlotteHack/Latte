package com.salad.latte.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.salad.latte.R

class WithdrawDialogFragment() :DialogFragment() {

    lateinit var tv_amountForWithdrawl :TextView
    lateinit var tv_amount :TextView
    lateinit var seek :SeekBar
    lateinit var btn_submit :Button
    lateinit var btn_cancel :Button
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_fragment_withdrawal,null)
        tv_amountForWithdrawl = view.findViewById<TextView>(R.id.tv_dialog_withdrawl_avail)
        tv_amount = view.findViewById<TextView>(R.id.dialog_tv_amount)
        seek = view.findViewById<SeekBar>(R.id.dialog_seek)
        btn_submit = view.findViewById<Button>(R.id.dialog_btn_submit)
        btn_cancel = view.findViewById<Button>(R.id.dialog_button_cancel)

        return AlertDialog.Builder(activity).setView(view).setTitle("Test").create()
    }

}