package com.salad.latte.Dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForgotPasswordDialogFragment : DialogFragment() {
    lateinit var cancelBtn : Button
    lateinit var sendBtn : Button
    lateinit var resetEmailET :EditText
    lateinit var firebaseDB: FirebaseDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.dialog_forgot_password,container,false)
        firebaseDB = FirebaseDB()
        cancelBtn = v.findViewById(R.id.btn_cancel_reset)
        sendBtn = v.findViewById(R.id.btn_send_reset)
        resetEmailET = v.findViewById(R.id.et_reset_email)

        cancelBtn.setOnClickListener {
            dismiss()
        }
        sendBtn.setOnClickListener {
            lifecycleScope.launch {
                if (isValidEmail()) {
                    firebaseDB.auth.sendPasswordResetEmail(resetEmailET.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                lifecycleScope.launch {
                                    Log.d("ForgotPasswordDialogFragment", "Email sent.")
                                    delay(3000)
                                    Toast.makeText(
                                        context,
                                        "Password reset email sent.",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    dismiss()
                                }
                            }
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Failed to reset email. Email must contain @ and .",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        return v
    }

    fun isValidEmail() : Boolean {
        var email = resetEmailET.text.toString()
        if(email.length > 0 && email.contains("@") && email.contains(".")){
            return true
        }
        return false
    }
}