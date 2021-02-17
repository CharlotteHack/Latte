package com.salad.latte.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.salad.latte.R

class AddDialogFragment() : DialogFragment() {

    lateinit var et_symbol : EditText
    lateinit var et_percent_equity: EditText
    lateinit var et_equity : EditText
    lateinit var btn_cancel :Button
    lateinit var dialogA :Dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_stock,null)
        et_symbol = view.findViewById<EditText>(R.id.et_symbol)
        et_percent_equity = view.findViewById<EditText>(R.id.et_percent)
        et_equity = view.findViewById<EditText>(R.id.et_equity)
        btn_cancel = view.findViewById(R.id.dialog_add_cancel_btn);
        dialogA = AlertDialog.Builder(activity).setView(view).setTitle("Test").create()


        btn_cancel.setOnClickListener(View.OnClickListener {
            dialogA.cancel()
//
        })


        return dialogA
    }

}