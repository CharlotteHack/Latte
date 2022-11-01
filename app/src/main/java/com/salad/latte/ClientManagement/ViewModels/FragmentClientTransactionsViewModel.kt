package com.salad.latte.ClientManagement.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.salad.latte.ClientManagement.FragmentClientTransactions
import com.salad.latte.ClientManagement.SampleAsset
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.ClientTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Double

class FragmentClientTransactionsViewModel(fct : FragmentClientTransactions) : ViewModel() {

    var mutableTransactions: MutableStateFlow<List<ClientTransaction>> =  MutableStateFlow(emptyList<ClientTransaction>())
    var immutableTransactions = mutableTransactions.asStateFlow()
    lateinit var firebaseDB: FirebaseDB
    lateinit var convertIDToFirebase: String

    init {
        viewModelScope.launch {
            firebaseDB = FirebaseDB()
            var id = firebaseDB.auth.currentUser!!.email
            convertIDToFirebase = id!!.replace(".", "|");
            firebaseDB.mDatabase.child("Clients").child(convertIDToFirebase).child("transactions")
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        var transactionsList = mutableListOf<ClientTransaction>()
//                    var saving = SampleAsset()
                        if(snapshot.childrenCount.toInt() == 0 ){
                            //No Transactions
                        }
                    else {
                            for (ds in snapshot.children) {
                                        var type = ds.child("type").getValue(String::class.java)
                                        var amount =
                                            Double.valueOf(ds.child("amount").getValue(Long::class.java).toString())
                                        var status = ds.child("status").getValue(String::class.java)
                                        var timestamp = ds.child("timestamp").getValue(Long::class.java)
                                        Log.d(
                                            "(39) FragmentClientTransactionsViewModel",
                                            "Amount: " + amount
                                        )
                                        transactionsList.add(
                                            ClientTransaction(
                                                type!!,
                                                amount,
                                                status!!,
                                                timestamp!!
                                            )
                                        )
                                    }
                            mutableTransactions.value = transactionsList
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        viewModelScope.launch {
                            delay(10000)
                            Toast.makeText(fct.requireContext(),error.message,Toast.LENGTH_LONG).show()
//                    }
                        }
                    }

                })

        }
    }
}