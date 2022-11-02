package com.salad.latte.ClientManagement.ViewModels

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.salad.latte.ClientManagement.ActivityClient
import com.salad.latte.ClientManagement.ActivityWithdrawalSuccessful
import com.salad.latte.ClientManagement.SignupActivity
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.HashMap

const val TAG = "ActivitySignupViewModel"
class ActivitySignupViewModel(act :SignupActivity) : ViewModel() {
    lateinit var firebaseDB: FirebaseDB
    var act = act
    init {
        firebaseDB = FirebaseDB()
    }

    suspend fun createStripeClient(){

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
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("accountID").setValue("none")


        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("isIBKRLinked").setValue(false)
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("isTwoSumsRequested").setValue(false)
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("twoSumsVerification").setValue(listOf(0,0))
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("userBankAccountNumber").setValue("0")
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase)
            .child("userRoutingNumber").setValue("0")
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        ContentValues.TAG,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }
                val key: String =
                    firebaseDB.mDatabase.push().getKey()!!
                val hashy =
                    HashMap<String, String?>()
                hashy["action"] = client.client_email+" account created. Create IBKR Account and like that users AccountID and send push notification if successful"
                hashy["email"] = client.client_email
                hashy["token"] = task.result
                firebaseDB.mDatabase.child("RequestQueue").child(key)
                    .setValue(hashy).addOnSuccessListener {
                        //The request succeeded, now u can show user success page
                        this@ActivitySignupViewModel.viewModelScope.launch {
                            var activity = this@ActivitySignupViewModel
                            delay(3000)
                            var intent = Intent(act,
                                ActivityClient::class.java)
                            act.startActivity(intent)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(act.applicationContext,"Failed to create user. Please visit our discord",Toast.LENGTH_LONG).show()
                    }
            })

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
                    Toast.makeText(context, task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }

    }
}