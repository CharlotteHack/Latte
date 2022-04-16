package com.salad.latte

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.salad.latte.Adapters.DailyWatchlistAdapter
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.DailyWatchlistItem

class DailyWatchlistFragment : Fragment() {

    lateinit var dailyWatchRV :RecyclerView
    lateinit var postReference : DatabaseReference
    var items = ArrayList<DailyWatchlistItem>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_daily_watchlist,container,false)
        postReference = Firebase.database.reference
        Log.d("DailyWatchlistFragment",postReference.child("daily_picks").database.toString()+"")
//        postReference.addValueEventListener(getDailyPickItems(postReference))
//        postReference.child("test").setValue("pewp")
        var firebaseDB = FirebaseDB()
        dailyWatchRV = v.findViewById(R.id.daily_watch_rv)
//        items.add(DailyWatchlistItem("","MSFA",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFB",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFC",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFD",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFE",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFF",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFG",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFH",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFI",0.0f,0.0f,0.0f))
//        items.add(DailyWatchlistItem("","MSFJ",0.0f,0.0f,0.0f))
        items.addAll(firebaseDB.pullDailyData(requireContext(),dailyWatchRV))
//        var adapter = DailyWatchlistAdapter(items,requireContext())
//        dailyWatchRV.layoutManager = LinearLayoutManager(activity)
//        dailyWatchRV.adapter = adapter
//        adapter.notifyDataSetChanged()
        return v;
    }

    private fun getDailyPickItems(postRef: DatabaseReference) : ValueEventListener{
        val postListener = object : ValueEventListener {


            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                items.clear()
                Log.d("DailyWatchlistFragment","boop")

                dataSnapshot.children.forEach({
                    Log.d("DailyWatchlistFragment",it.key+"")
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("DailyWatchlistFragment", "loadPost:onCancelled", databaseError.toException())
            }
        }

        Log.d("DailyWatchlistFragment","boop 2 "+postRef.get())
        return postListener


    }
}