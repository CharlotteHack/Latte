package com.salad.latte.ClientManagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.salad.latte.ClientManagement.ViewModels.LoginActivityViewModel
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.R
import com.salad.latte.databinding.ActivityLoginBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    private val loginViewModel :LoginActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel.firebaseDB.isAuthenticated("LoginActivity")

        binding.apply {
            binding.buttonSignup.setOnClickListener {
                var intent = Intent(this@LoginActivity,SignupActivity::class.java)
                startActivity(intent)
            }
            binding.btnLogin.setOnClickListener {
                var email = binding.etEmail.text.toString()
                var password = binding.etPassword.text.toString()
                if(loginViewModel.runChecks(this@LoginActivity.baseContext,email, password)){
                    loginViewModel.firebaseDB.auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@LoginActivity) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LoginActivity", "signInWithEmail:success")
                                val user = loginViewModel.firebaseDB.auth.currentUser
//                                updateUI(user)
                                Log.d("LoginActivity","Navigating to Client Dashboard")
                                var intent = Intent(this@LoginActivity,ActivityClient::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
//                                updateUI(null)
                            }
                        }
                }
            }
            binding.tvForgotPassword.setOnClickListener {

                val dialog = LayoutInflater.from(this@LoginActivity).inflate(R.layout.dialog_forgot_password,null,false)

                val builder = AlertDialog.Builder(dialog.context).create()
                dialog.findViewById<Button>(R.id.btn_cancel_reset)!!.setOnClickListener {
                    builder.dismiss()
                }
                dialog.findViewById<Button>(R.id.btn_send_reset)!!.setOnClickListener {
                    fun isValidEmail(email :String) : Boolean {
                        if(email.length > 0 && email.contains("@") && email.contains(".")){
                            return true
                        }
                        return false
                    }
                    lifecycleScope.launch {
                        var email = dialog.findViewById<EditText>(R.id.et_reset_email)!!.text.toString()
                        if (isValidEmail(email)) {
                            FirebaseDB().auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        lifecycleScope.launch {
                                            Log.d("ForgotPasswordDialogFragment", "Email sent.")
                                            delay(3000)
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Password reset email sent.",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            builder.dismiss()
                                        }
                                    }
                                }
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Failed to reset email. Email must contain @ and .",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                builder.setView(dialog)
                builder.show()
            }

        }

    }
}