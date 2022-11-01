package com.salad.latte.ClientManagement.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.salad.latte.ClientManagement.FragmentClientSettings
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.Client
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class FragmentSettingsViewModel(db: FirebaseDB, activity: FragmentClientSettings) : ViewModel() {
    var firebaseDB = db
    var activity = activity
    var mutableClientFlow: MutableStateFlow<List<Client>> = MutableStateFlow(listOf<Client>())
    var clientStateFlow = mutableClientFlow.asStateFlow()

    init {
        viewModelScope.launch {
            initClient()

        }
    }


    suspend fun initClient() {
        viewModelScope.launch {
            //Get customer client account
            if (firebaseDB.auth.currentUser != null) {
                var _email = firebaseDB.auth.currentUser!!.email!!.replace(".", "|");
                firebaseDB.mDatabase.child("Clients").child(_email)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            Log.i("FragmentSettingViewModel", "Got value ${snapshot.value}")
                            if (snapshot.value != null) {
                                var client = Client()
                                client.client_name = (snapshot.child("name").value as? String)!!
                                client.client_email = firebaseDB.auth.currentUser!!.email!!
                                client.client_balance =
                                    (snapshot.child("accountValue").value as? String)!!
                                client.client_account_number =
                                    (snapshot.child("accountID").value as? String)!!
                                client.client_bank_account_number =
                                    (snapshot.child("userBankAccountNumber").value as? String)!!
                                client.client_routing_number =
                                    (snapshot.child("userRoutingNumber").value as? String)!!
                                client.client_isibkr_linked =
                                    (snapshot.child("isIBKRLinked").value as? Boolean)!!
                                mutableClientFlow.value = listOf(client)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            viewModelScope.launch {
                                delay(10000)
                                initClient()
//                    }
                            }
                        }
//                }.addOnFailureListener {
//                    Log.e("FragmentSettingViewModel", "Error getting data, retrying..", it)
////                    Toast.makeText(
////                        activity.requireContext(),
////                        it.message.toString(),
////                        Toast.LENGTH_LONG
////                    )
////                        .show()
//                    viewModelScope.launch {
//                        delay(10000)
//                        initClient()
//                    }
//                }
                    })
            }
        }
    }
}




