package com.salad.latte.ClientManagement.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityVerifyDepositAccountViewModel(email :String) : ViewModel() {

    var mutableClient = MutableStateFlow<Client>(Client())
    var immutableClient = mutableClient.asStateFlow()
    var emailKey = email.replace(".", "|")
    var firebaseDB = FirebaseDB()

    init {

    }

    suspend fun fetchClient() {
        viewModelScope.launch {
            try {
                var mDatabase = firebaseDB.mDatabase
                mDatabase.child("Clients").child(emailKey).get().addOnSuccessListener {
                    Log.i("ActivityVerifyDepositAccountViewModel", "Got value ${it.value}")
                }.addOnFailureListener {
                    Log.e("ActivityVerifyDepositAccountViewModel", "Error getting data", it)
                }
                mutableClient.value = Client()
            } catch (e: Exception) {
                Log.d("ActivityVerifyDepositAccountViewModel", e.message!!.toString())
            }
        }
    }
}