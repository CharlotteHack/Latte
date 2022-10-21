package com.salad.latte.ClientManagement.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salad.latte.Database.FirebaseDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityClientViewModel : ViewModel() {

    var ibkrClientIDMutableStateFlow : MutableStateFlow<String> = MutableStateFlow("false")
    var ibkrClientIDStateFlow = ibkrClientIDMutableStateFlow.asStateFlow()
    lateinit var firebaseDB : FirebaseDB


    init {
        viewModelScope.launch {
            firebaseDB = FirebaseDB()
            if (firebaseDB.auth.currentUser != null){
            getIBKRAccount()
            }
        }
    }

    suspend fun getIBKRAccount(){
        var convertIDToFirebase = firebaseDB.auth.currentUser!!.email!!.replace(".","|");
        firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child("accountID").get().addOnSuccessListener {
            Log.i("ActivityClientViewModel", "Got value ${it.value}")
            ibkrClientIDMutableStateFlow.value = it.getValue(String::class.java)!!;
        }
    }
}