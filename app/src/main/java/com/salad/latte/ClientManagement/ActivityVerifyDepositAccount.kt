package com.salad.latte.ClientManagement

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.salad.latte.ClientManagement.ViewModels.ActivityVerifyDepositAccountViewModel
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import com.salad.latte.R
import com.salad.latte.databinding.ActivityVerifyDepositAccountBinding
import kotlinx.coroutines.launch

class ActivityVerifyDepositAccount : AppCompatActivity(){

    lateinit var viewModel :ActivityVerifyDepositAccountViewModel
    lateinit var binding :ActivityVerifyDepositAccountBinding
    private var mDatabase = FirebaseDB().mDatabase
    private var firebaseDB = FirebaseDB()

    lateinit var email :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.hasExtra("email")){
            email = intent.getStringExtra("email")!!
            viewModel = ActivityVerifyDepositAccountViewModel(intent.getStringExtra("email")!!)
            binding = ActivityVerifyDepositAccountBinding.bind(layoutInflater.inflate(R.layout.activity_verify_deposit_account,null,false))
            setContentView(binding.root)
            lifecycleScope.launch {
                viewModel.fetchClient()

                //When firebase is done fetching data
                viewModel.immutableClient.collect {
                    Log.i("ActivityVerifyDepositAccount", "Got value ${it}")
                   if(it.client_bank_account_number != "0"){
                        binding.verifyBanEt.setText(it.client_bank_account_number)
                   }
                    if(it.client_routing_number != "0"){
                        binding.verifyBanEt.setText(it.client_routing_number)
                    }
                    if(it.client_routing_number != "0" && it.client_bank_account_number != "0"){
                        binding.constraintLayout13.visibility = View.VISIBLE
                    }
                    if(it.verfiedSums[0] != 0.0){
                        binding.verifyAmountOneEt.setText(it.verfiedSums[0].toString())
                    }
                    if(it.verfiedSums[1] != 0.0){
                        binding.verifyAmountTwoEt.setText(it.verfiedSums[1].toString())
                    }
                    if(it.verfiedSums[0] != 0.0 && it.verfiedSums[1] != 0.0){
                        binding.verifyTwosumsFinalTv.visibility = View.VISIBLE

                    }
                }
            }
        }
        //If user is entering information

        binding.apply {
            closeBanBtn.setOnClickListener { 
                finish()
            }
            twosumsCloseBtn.setOnClickListener {
                finish()
            }
            confirmBanBtn.setOnClickListener {
                //Make sure Routing & Ban are not null and atleast 6 chars each
                if(validateRoutingNumber() && validateBan()){
                    //Write the Routing & BAN to firebase
                    //Set a request to process Routing & Ban
                    //Unhide the view for verify two sums
                    pushBankInfoToFirebase(binding.verifyBanEt.text.toString(),binding.verifyRoutingEt.text.toString())
                }

            }
            
            twosumsVerifyBtn.setOnClickListener {
                //Make sure Two sums are not null and are doubles
                if(validateTwoSums()){
                    //Write two sums into firebase
                    var sumOneDouble = binding.verifyAmountOneEt.text.toString().toDoubleOrNull()
                    var sumTwoDouble = binding.verifyAmountTwoEt.text.toString().toDoubleOrNull()
                    pushTwoSumInfoToFirebase(sumOneDouble!!,sumTwoDouble!!)
                    //Set a request to process two sums
                    //Unhide the view that tells users its safe to close the page
                }

            }
        }


    }

    fun validateRoutingNumber() : Boolean{
        if(binding.verifyRoutingEt.text.toString() == ""){
            Toast.makeText(this,"Routing Number cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        else if(binding.verifyRoutingEt.text.toString().length < 6){

            Toast.makeText(this,"Routing Number must be atleast 6 numbers",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun validateBan() : Boolean{
        if(binding.verifyBanEt.text.toString() == ""){
            Toast.makeText(this,"Account Number cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        else if(binding.verifyBanEt.text.toString().length < 6){

            Toast.makeText(this,"Account Number must be atleast 6 numbers",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun validateTwoSums() : Boolean {
        var sumOne = binding.verifyAmountOneEt.text.toString()
        var sumTwo = binding.verifyAmountTwoEt.text.toString()
        if(sumOne == ""){
            Toast.makeText(this,"Amount one cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        if(sumTwo == ""){
            Toast.makeText(this,"Amount two cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        var sumOneDouble = sumOne.toDoubleOrNull()
        var sumTwoDouble = sumTwo.toDoubleOrNull()
        if(sumOneDouble == null){
            Toast.makeText(this,"Amount one must be a number with decimal",Toast.LENGTH_LONG).show()
            return false
        }
        if(sumTwoDouble == null){
            Toast.makeText(this,"Amount two must be a number with decimal",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun pushBankInfoToFirebase(ban :String, rn :String){
        var emailKey = email.replace(".", "|")
        mDatabase.child("Clients").child(emailKey).child("userBankAccountNumber").setValue(ban).addOnSuccessListener {
            mDatabase.child("Clients").child(emailKey).child("userRoutingNumber").setValue(rn).addOnSuccessListener {
                //Finished pushing data
                firebaseDB.addToRequestQueue("User added RN and BN, update IBKR with that info, then send push notification",email)
                binding.constraintLayout13.visibility = View.VISIBLE

            }.addOnFailureListener {
                Toast.makeText(this,"Failed to add Routing Number",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to add Bank Account Number",Toast.LENGTH_LONG).show()

        }
    }

    fun pushTwoSumInfoToFirebase(amoOne :Double, amoTwo :Double){
        var emailKey = email.replace(".", "|")
        mDatabase.child("Clients").child(emailKey).child("twoSumsVerification").setValue(listOf(amoOne,amoTwo)).addOnSuccessListener {
            firebaseDB.addToRequestQueue("User is trying to verify two sums, update IBKR with that info. Set isIBKRLinked to true, then send push notification if successful",email)
            binding.verifyTwosumsFinalTv.visibility = View.VISIBLE
        }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to verify two amounts",Toast.LENGTH_LONG).show()

            }
    }


}