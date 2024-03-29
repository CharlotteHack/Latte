package com.salad.latte.ClientManagement

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.salad.latte.ClientManagement.ViewModels.FragmentClientViewModel
import com.salad.latte.R
import com.salad.latte.databinding.FragmentClientHomeBinding
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class FragmentClientDashboard : Fragment(), OnSeekBarChangeListener {

    lateinit var viewModel : FragmentClientViewModel
    lateinit var binding : FragmentClientHomeBinding
    private val chart: LineChart? = null
    private val seekBarX: SeekBar? = null
    private  var seekBarY:SeekBar? = null
    private val tvX: TextView? = null
    private  var tvY:TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_client_home,container,false)
        binding = FragmentClientHomeBinding.bind(v);
        binding.rvAssets.layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
        lifecycleScope.launch {
            viewModel = FragmentClientViewModel(this@FragmentClientDashboard)
            viewModel.assetsStateFlow.collect{
                var adapter = AssetsAdapter(it,requireContext())
                binding.rvAssets.adapter = adapter
                Log.d("FragmentClientDashboard","Assets size: "+it.size.toString())
            }
        }
        lifecycleScope.launch {
            viewModel.ibkrClientIDStateFlow.collect {
                Log.d("FragmentClientDashboard","Account ID: "+it.toString())
                if(it == "none"){
                    binding.toolbar4.visibility = View.VISIBLE
                    binding.confirmationText.visibility = View.VISIBLE
                    binding.nosavingsView.visibility = View.GONE
                }
                else if(it == "") {
                    binding.toolbar4.visibility = View.INVISIBLE
                    binding.confirmationText.visibility = View.INVISIBLE

                }
            }
        }


        return binding.root;
    }

    class AssetsViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

        lateinit var amount : TextView
        lateinit var img :ImageView
        lateinit var pb :ProgressBar
        lateinit var date : TextView

        init {
            amount = itemView.findViewById(R.id.tv_amount_asset)
            img = itemView.findViewById(R.id.iv_image_asset)
            pb = itemView.findViewById(R.id.pb_assets)
            date = itemView.findViewById(R.id.tv_date_asset)
        }

    }

    class AssetsAdapter(itemsList :List<SampleAsset>, context : Context) : RecyclerView.Adapter<AssetsViewHolder>(){
        var items = itemsList
        var ctx = context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetsViewHolder {
            var v = LayoutInflater.from(ctx).inflate(R.layout.custom_sample_asset,parent,false)
            return AssetsViewHolder(v)
        }

        override fun onBindViewHolder(holder: AssetsViewHolder, position: Int) {
            var item = items.get(position)
            holder.amount.setText(currencyFormat(item.amount.toString()))
            holder.date.setText(item.date)
//            holder.img.load(item.imgurl) {
//                placeholder(R.drawable.loading)
//            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        fun currencyFormat(amount: String): String? {
            val formatter = DecimalFormat("###,###,##0.00")
            return formatter.format(amount.toDouble())
        }
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }


}