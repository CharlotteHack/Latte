package com.salad.latte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.SuperInvestor.SuperInvestor

class SuperInvestorsFragment  : Fragment() {
    lateinit var firebaseDB :FirebaseDB
    lateinit var superInvestors :ArrayList<SuperInvestor>
    lateinit var superinvestors_lv :ListView
    lateinit var superinvestors_progress :ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseDB = FirebaseDB();
        superInvestors = ArrayList();

        val view = inflater.inflate(R.layout.fragment_superinvestors,container,false)
        superinvestors_lv = view.findViewById(R.id.superinvestors_lv);
        superinvestors_progress = view.findViewById(R.id.superinvestor_progress)
        superInvestors.addAll(firebaseDB.pullSuperInvestorData(context,R.layout.fragment_superinvestors,superinvestors_lv,superinvestors_progress))


        return view;
    }


}