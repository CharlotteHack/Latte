package com.salad.latte.ClientManagement.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salad.latte.ClientManagement.SampleAsset
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.ClientTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Double

class FragmentClientTransactionsViewModel : ViewModel() {

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
                .get().addOnSuccessListener {
                var transactionsList = mutableListOf<ClientTransaction>()
                    var transactions = it.value as? HashMap<*, *>
                Log.d("(29) FragmentClientTransactionsViewModel", "Transactions: " + transactions)
//                    var saving = SampleAsset()
                if ( transactions != null) {

                    transactions.forEach { (key, value) ->
                        var transaction = value as HashMap<String, *>
                        var type = transaction.get("type") as String
                        var amount = Double.valueOf((transaction.get("amount") as Long).toString())
                        var status = transaction.get("status") as String
                        var timestamp = transaction.get("timestamp") as Long
                        Log.d("(39) FragmentClientTransactionsViewModel", "Amount: " + amount)
                        transactionsList.add(ClientTransaction(type, amount, status, timestamp))
                    }
                    mutableTransactions.value = transactionsList
                }

            }

        }
    }
}