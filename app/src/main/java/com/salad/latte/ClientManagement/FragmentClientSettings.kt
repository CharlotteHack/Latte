package com.salad.latte.ClientManagement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.salad.latte.ClientManagement.ViewModels.FragmentSettingsViewModel
import com.salad.latte.ClientManagement.ViewModels.TAG
import com.salad.latte.ClientManagement.api.StripeApi
import com.salad.latte.Database.FirebaseDB
//import com.braintreepayments.api.BraintreeClient
//import com.braintreepayments.api.ClientTokenCallback
//import com.braintreepayments.api.ClientTokenProvider
import com.salad.latte.R
import com.salad.latte.databinding.FragmentClientSettingsBinding
import com.salad.latte.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

//import retrofit2.converter.gson.GsonConverterFactory

class FragmentClientSettings : Fragment() {
    final var stripeID = "U4693996"
    lateinit var firebaseDB :FirebaseDB
    lateinit var stripeAPI : StripeApi
    lateinit var retrofit : Retrofit
    lateinit var viewModel : FragmentSettingsViewModel
    lateinit var binding : FragmentClientSettingsBinding
    var isAccountIBKRVerified = false

//    private lateinit var braintreeClient: BraintreeClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        var v = inflater.inflate(R.layout.fragment_client_settings,container,false)
        binding = FragmentClientSettingsBinding.inflate(inflater)
        firebaseDB = FirebaseDB()
        viewModel = FragmentSettingsViewModel(firebaseDB, this@FragmentClientSettings)
        retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-latte-d25b7.cloudfunctions.net/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        stripeAPI = retrofit.create<StripeApi>()

        lifecycleScope.launch {
          var uiUpdate = viewModel.clientStateFlow.collect {
              if(it.size > 0) {
                  var client = it.get(0)
                  Log.d("FragmentClientSettings","Account: "+client.toString())

                  binding.tvClientAccountbalanceSettings.setText("Net Account Value: "+client.formatAccountValue())
                  binding.etClientFirstname.setText(client.client_name)
                  binding.tvClientEmailSettings.setText("Email: "+client.client_email)
                 if(client.client_account_number.equals("none")) {
                     isAccountIBKRVerified = false
                 } else {
                     isAccountIBKRVerified = true
                 }
                  Log.d("FragmentClientSettings","Is Account IBKR Verified: "+isAccountIBKRVerified)
                  binding.apply {
//                      automaticDepositsBtn.visibility = View.INVISIBLE
//                      tvAutomaticInvestTitle.visibility = View.INVISIBLE
                      clientLogoutButton.setOnClickListener {
                          firebaseDB.auth.signOut()
                          var intent = Intent(this@FragmentClientSettings.context,LoginActivity::class.java)
                          startActivity(intent)
                      }
                      clientDepositButton.setOnClickListener {
                          if (isAccountIBKRVerified) {
                              var builder =
                                  AlertDialog.Builder(this@FragmentClientSettings.requireContext())
                              var customView = LayoutInflater.from(requireContext())
                                  .inflate(R.layout.dialog_invoice_deposit, null)
                              builder.setView(customView)
                              var createdBuilder = builder.create()
                              customView.findViewById<Button>(R.id.invoice_cancel_btn)
                                  .setOnClickListener {
                                      Toast.makeText(requireContext(), "Dismiss", Toast.LENGTH_LONG)
                                          .show()
                                      createdBuilder.dismiss()
                                  }
                              createdBuilder.show()

                              //Create transaction and save paymentIntent on firebase.
                              val url =
                                  URL("https://us-central1-latte-d25b7.cloudfunctions.net/createACHDeposit?stripeid=U4693996?amount=5")
                              Log.d("FragmentClientSettings", "Payment URL: " + url)
                              Toast.makeText(context, "Deposited!", Toast.LENGTH_LONG).show()
                              lifecycleScope.launch {
                                  var deposit = stripeAPI.createDeposit("U4693996", 5)
                                  Log.d("FragmentClientSettings: ", deposit.toString())
                              }
                          } else {
                              Toast.makeText(requireContext(),"Account not verified yet.",Toast.LENGTH_LONG).show()
                          }
                      }
                      btnDiscord.setOnClickListener {
                          var intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://discord.gg/mPRrvDTn7k"))
                          startActivity(intent)

                      }
//                      automaticDepositsBtn.setOnClickListener {
//                          Toast.makeText(context,"Automatic deposits are not rolled out yet",Toast.LENGTH_LONG).show()
//                      }
                      btnWithdrawalSettings.setOnClickListener {
                          if (isAccountIBKRVerified) {
                              var builder =
                                  AlertDialog.Builder(this@FragmentClientSettings.requireContext())
                              var customView = LayoutInflater.from(requireContext())
                                  .inflate(R.layout.dialog_client_withdraw, null)
                              builder.setView(customView)
                              var createdBuilder = builder.create()
                              customView.findViewById<Button>(R.id.withdrawal_cancel_btn)
                                  .setOnClickListener {
                                      Toast.makeText(requireContext(), "Dismiss", Toast.LENGTH_LONG)
                                          .show()
                                      createdBuilder.dismiss()
                                  }
                              createdBuilder.show()

                          } else {
                              Toast.makeText(requireContext(),"Account not verified yet.",Toast.LENGTH_LONG).show()
                          }
                      }
                  }
              }
          }
        }
//        braintreeClient = BraintreeClient(requireContext(), "sandbox_tvrwq73x_2rd4b67txtr9drx2")
//        braintreeClient = BraintreeClient(requireContext(), ExampleClientTokenProvider())
        return binding.root;

    }



//    internal class ExampleClientTokenProvider : ClientTokenProvider {
//        override fun getClientToken(callback: ClientTokenCallback) {
//            val call: Call<ClientToken> = createService().getClientToken()
//            call.enqueue(object : Callback<ClientToken?> {
//                override fun onResponse(call: Call<ClientToken?>?, response: Response<ClientToken?>?) {
//                    response?.body()?.value?.let { callback.onSuccess(it) }
//                }
//
//                override fun onFailure(call: Call<ClientToken?>?, t: Throwable?) {
//                    callback.onFailure(Exception(t))
//                }
//            })
//        }

//        companion object {
//
//            private val builder = Retrofit.Builder()
//                    .baseUrl("https://my-api.com")
//                    .addConverterFactory(GsonConverterFactory.create())
//            private val httpClient = OkHttpClient.Builder()
//            fun createService(): Api {
//                builder.client(httpClient.build())
//                val retrofit = builder.build()
//                return retrofit.create(Api::class.java)
//            }
//        }
//    }
}