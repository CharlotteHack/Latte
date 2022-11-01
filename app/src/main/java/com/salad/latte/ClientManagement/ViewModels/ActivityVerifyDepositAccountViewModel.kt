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
                    var client = Client()
                    client.client_bank_account_number = it.child("userBankAccountNumber").value as String
                    client.client_account_number = it.child("accountID").value as String
                    client.client_unrealized_profit = it.child("unrealizedValue").toString()
                    client.client_balance = (it.child("accountValue").value).toString()
                    client.client_isibkr_linked = it.child("isIBKRLinked").value as Boolean
                    client.client_isTwoSumsRequested = it.child("isTwoSumsRequested").value as Boolean
                    client.verfiedSums = it.child("twoSumsVerification").value as List<Double>
                    client.client_name = it.child("name").value as String
                    client.client_routing_number = it.child("userRoutingNumber").value as String
                    client.client_email = it.child("email").value as String
                    mutableClient.value = client

                }.addOnFailureListener {
                    Log.e("ActivityVerifyDepositAccountViewModel", "Error getting data", it)
                }
            } catch (e: Exception) {
                Log.d("ActivityVerifyDepositAccountViewModel", e.message!!.toString())
            }
        }
    }
}