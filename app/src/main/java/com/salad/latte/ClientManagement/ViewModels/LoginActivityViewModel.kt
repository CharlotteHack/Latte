package com.salad.latte.ClientManagement.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.salad.latte.Database.FirebaseDB

class LoginActivityViewModel : ViewModel() {
    val firebaseDB = FirebaseDB()

    fun checkEmailLength(email :String, context: Context) : Boolean{
        if(email.length <= 3){
            Toast.makeText(context,"Email is less then 4 characters",Toast.LENGTH_LONG).show()

            return false
        }
        return true
    }
    fun checkEmailForAtSign(email :String, context: Context) : Boolean {
        if(!email.contains("@")){
            Toast.makeText(context,"Email missing @ symbol",Toast.LENGTH_LONG).show()

            return false
        }
        return true
    }
    fun checkEmailForDotSign(email: String, context: Context) : Boolean {
        if(!email.contains(".")){
            Toast.makeText(context,"Email missing . symbol",Toast.LENGTH_LONG).show()

            return false
        }
        return true
    }
    fun checkPasswordLength(password :String, context :Context) : Boolean{
        if(password.length <= 5){
            Toast.makeText(context,"Password is less then 6 characters",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun runChecks(context : Context, email: String, password: String) : Boolean{
        if(checkPasswordLength(password, context) &&
                checkEmailForDotSign(email, context) &&
                    checkEmailForAtSign(email, context) &&
                    checkEmailLength(email, context)
                ){
            return true
        }
        return false
    }

}