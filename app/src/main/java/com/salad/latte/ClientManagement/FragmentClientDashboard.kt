package com.salad.latte.ClientManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.salad.latte.ClientManagement.ViewModels.FragmentClientViewModel
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.R
import com.salad.latte.databinding.FragmentClientHomeBinding
import kotlinx.coroutines.launch

class FragmentClientDashboard : Fragment() {

    lateinit var viewModel : FragmentClientViewModel
    lateinit var binding : FragmentClientHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_client_home,container,false)
        binding = FragmentClientHomeBinding.bind(v);
        lifecycleScope.launch {
            viewModel = FragmentClientViewModel(this@FragmentClientDashboard)

        }
        return binding.root;
    }






}