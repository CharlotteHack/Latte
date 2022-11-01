package com.salad.latte.ClientManagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.ClientManagement.ViewModels.FragmentClientTransactionsViewModel
import com.salad.latte.Objects.ClientTransaction
import com.salad.latte.R
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentClientTransactions : Fragment() {
    lateinit var transactionsRv :RecyclerView
    lateinit var transactions :ArrayList<Transaction>
    lateinit var clientTransactionAdapter :ClientTransactionsAdapter
    lateinit var transactionsViewModel: FragmentClientTransactionsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_client_transactions,container,false)
        transactionsRv = v.findViewById(R.id.client_transactions_rv)
        transactionsViewModel = FragmentClientTransactionsViewModel()
        lifecycleScope.launch {
            transactionsViewModel.immutableTransactions.collect() {
                transactionsRv.layoutManager = LinearLayoutManager(activity)
                clientTransactionAdapter = ClientTransactionsAdapter(it,requireContext(),R.layout.custom_client_transaction)
                transactionsRv.adapter = clientTransactionAdapter
                clientTransactionAdapter.notifyDataSetChanged()

            }
        }

        return v
    }

    public class ClientTransactionsAdapter(trans: List<ClientTransaction>, con : Context, rootLayout :Int) : RecyclerView.Adapter<ClientTransactionsViewHolder>() {

        var transactions = trans
        var context = con
        var root = rootLayout
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientTransactionsViewHolder {
            return ClientTransactionsViewHolder(LayoutInflater.from(context).inflate(root,parent,false))

        }

        override fun onBindViewHolder(holder: ClientTransactionsViewHolder, position: Int) {
            holder.client_date.setText(getDateTimeAsFormattedString(transactions.get(position).timestamp))
            holder.client_title.setText(transactions.get(position).type.capitalize()+" of $"+currencyFormat(transactions.get(position).amount.toString()))
            holder.client_status.setText(transactions.get(position).status)
        }

        override fun getItemCount(): Int {
            return transactions.size
        }

        fun currencyFormat(amount: String): String? {
            val formatter = DecimalFormat("###,###,##0.00")
            return formatter.format(amount.toDouble())
        }

        private fun getDateTimeAsFormattedString(dateAsLongInMs: Long): String? {
            try {
                return SimpleDateFormat("MM/dd/yyyy").format(Date(dateAsLongInMs))
            } catch (e: Exception) {
                return null // parsing exception
            }
        }
    }

    public class ClientTransactionsViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView) {

        lateinit var client_title :TextView
        lateinit var client_date :TextView
        lateinit var client_status :TextView

        init {
            client_title = itemView.findViewById(R.id.client_transaction_title)
            client_date = itemView.findViewById(R.id.client_transaction_date)
            client_status = itemView.findViewById(R.id.client_transaction_status)
        }

    }


}