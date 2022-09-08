package com.salad.latte

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.salad.latte.Database.FirebaseDB

class ActivityLogin : AppCompatActivity() {

    lateinit var email_et :EditText
    lateinit var password_et :EditText
    lateinit var signInBtn :Button
    private var firebaseDB = FirebaseDB()
    private var auth = firebaseDB.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // ...
        // Initialize Firebase Auth

        email_et = findViewById(R.id.et_email)
        password_et = findViewById(R.id.et_password)
        signInBtn = findViewById(R.id.btn_login)

        signInBtn.setOnClickListener {
                login(email_et.text.toString(),password_et.text.toString())
        }



    }

    fun login(email: String, password: String) {
        Log.d("ActivityLogin","Email: "+email)
        Log.d("ActivityLogin", "Password: "+password)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("ActivityLogin", "signInWithEmail:success")
                        val user: FirebaseUser = auth.getCurrentUser()!!
                        //                            updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("ActivityLogin", "signInWithEmail:failure", task.exception)
                        //                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                        //                                    Toast.LENGTH_SHORT).show();
                        //                            updateUI(null);
                    }
                })
    }
}