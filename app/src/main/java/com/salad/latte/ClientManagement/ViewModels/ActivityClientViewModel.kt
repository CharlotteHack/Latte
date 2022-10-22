package com.salad.latte.ClientManagement.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salad.latte.Database.FirebaseDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityClientViewModel : ViewModel() {


    lateinit var firebaseDB : FirebaseDB


    init {
        viewModelScope.launch {
            firebaseDB = FirebaseDB()
            if (firebaseDB.auth.currentUser != null){

            }
        }
    }


}