package com.salad.latte.ClientManagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.salad.latte.ClientManagement.ViewModels.LoginActivityViewModel
import com.salad.latte.databinding.ActivityLoginBinding
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
                if(loginViewModel.runChecks()){
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

        }
    }
}