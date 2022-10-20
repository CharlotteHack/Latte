package com.salad.latte.ClientManagement.ViewModels

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salad.latte.ClientManagement.ActivityClient
import com.salad.latte.ClientManagement.SignupActivity
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import kotlinx.coroutines.launch

const val TAG = "ActivitySignupViewModel"
class ActivitySignupViewModel : ViewModel() {
    lateinit var firebaseDB: FirebaseDB
    init {
        firebaseDB = FirebaseDB()
    }


    suspend fun updateClientTWS(client: Client,context : SignupActivity){
        //Save user to firebase
        val convertIDToFirebase: String = client.client_email.replace(".", "|")
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("name").setValue(client.client_name)
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("email").setValue(client.client_email)
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("accountValue").setValue("0.0")
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("unrealizedValue").setValue("0.0")
        var intent = Intent(context,ActivityClient::class.java)
        context.startActivity(intent)
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
//                    updateUI(user)
                    viewModelScope.launch {
                        val user = firebaseDB.auth.currentUser
                        val convertIDToFirebase: String = user!!.email!!.replace(".", "|")
                        updateClientTWS(client,context)
                    }

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