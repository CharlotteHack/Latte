package com.salad.latte.Dialogs

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.salad.latte.R


class HowToBuyDialogFragment : DialogFragment() {

    lateinit var howtofinish_btn : Button
    lateinit var robinhood_btn :Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.dialog_how_to_buy_stocks, container, false);
        howtofinish_btn = v.findViewById(R.id.howtofinish_btn)
        robinhood_btn = v.findViewById(R.id.robinhood_navigate)
        robinhood_btn.setOnClickListener(View.OnClickListener {
            val ctx = context!! // or you can replace **'this'** with your **ActivityName.this**

            try {
                val i: Intent = ctx.getPackageManager().getLaunchIntentForPackage("com.robinhood.android")!!
                ctx.startActivity(i)
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        })
        howtofinish_btn.setOnClickListener(View.OnClickListener {
            dismiss()
        })
        return v;
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}