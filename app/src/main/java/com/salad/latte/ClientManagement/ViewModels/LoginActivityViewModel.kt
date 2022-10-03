package com.salad.latte.ClientManagement.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.salad.latte.Database.FirebaseDB

class LoginActivityViewModel : ViewModel() {
    val firebaseDB = FirebaseDB()

    fun checkEmailLength(email :String) : Boolean{
        if(email.length <= 3){
            return false
        }
        return true
    }
    fun checkEmailForAtSign(email :String) : Boolean {
        if(!email.contains("@")){
            return false
        }
        return true
    }
    fun checkEmailForDotSign(email: String) : Boolean {
        if(!email.contains(".")){
            return false
        }
        return true
    }
    fun checkPasswordLength(password :String) : Boolean{
        if(password.length <= 6){
            return false
        }
        return true
    }

    fun runChecks() : Boolean{
        return true
    }

}