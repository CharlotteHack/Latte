package com.salad.latte.ClientManagement.ViewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.salad.latte.ClientManagement.ActivityClient
import com.salad.latte.ClientManagement.SignupActivity
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client

const val TAG = "ActivitySignupViewModel"
class ActivitySignupViewModel : ViewModel() {
    lateinit var firebaseDB: FirebaseDB
    init {
        firebaseDB = FirebaseDB()
    }

    suspend fun createClient(client: Client, password :String,context : SignupActivity){
        //First signup the user
        //Create the client in firebase
        //Redirect user to login page
        firebaseDB.auth.createUserWithEmailAndPassword(client.client_email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = firebaseDB.auth.currentUser
//                    updateUI(user)
                    var intent = Intent(context,ActivityClient::class.java)
                    context.startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }

    }
}