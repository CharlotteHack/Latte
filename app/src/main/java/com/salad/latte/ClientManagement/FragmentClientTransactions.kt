package com.salad.latte.ClientManagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salad.latte.R

class FragmentClientTransactions : Fragment() {
    lateinit var transactionsRv :RecyclerView
    lateinit var transactions :ArrayList<Transaction>
    lateinit var clientTransactionAdapter :ClientTransactionsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_client_transactions,container,false)
        transactionsRv = v.findViewById(R.id.client_transactions_rv)
        transactions = ArrayList()
        transactions.add(Transaction("Deposit","9/11/2022","9/15/2022","3200","Pending"))
        transactionsRv.layoutManager = LinearLayoutManager(activity)
        clientTransactionAdapter = ClientTransactionsAdapter(transactions,context!!,R.layout.custom_client_transaction)
        transactionsRv.adapter = clientTransactionAdapter
        clientTransactionAdapter.notifyDataSetChanged()
        return v
    }

    public class ClientTransactionsAdapter(trans: ArrayList<Transaction>, con : Context, rootLayout :Int) : RecyclerView.Adapter<ClientTransactionsViewHolder>() {

        var transactions = trans
        var context = con
        var root = rootLayout
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientTransactionsViewHolder {
            return ClientTransactionsViewHolder(LayoutInflater.from(context).inflate(root,parent,false))

        }

        override fun onBindViewHolder(holder: ClientTransactionsViewHolder, position: Int) {
            holder.client_date.setText(transactions.get(position).dateRequested)
        }

        override fun getItemCount(): Int {
            return transactions.size
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