package com.salad.latte.ClientManagement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import kotlinx.coroutines.delay
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
import java.lang.Float
import java.net.URL

//import retrofit2.converter.gson.GsonConverterFactory

class FragmentClientSettings : Fragment() {
    lateinit var firebaseDB :FirebaseDB
    lateinit var stripeAPI : StripeApi
    lateinit var retrofit : Retrofit
    lateinit var viewModel : FragmentSettingsViewModel
    lateinit var binding : FragmentClientSettingsBinding
    var doesUserHaveAccountID = false

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
                     doesUserHaveAccountID = false
                 } else {
                     doesUserHaveAccountID = true
                 }
                  Log.d("FragmentClientSettings","Is Account ID Verified: "+doesUserHaveAccountID)
                  binding.apply {
//                      automaticDepositsBtn.visibility = View.INVISIBLE
//                      tvAutomaticInvestTitle.visibility = View.INVISIBLE
                      clientLogoutButton.setOnClickListener {
                          firebaseDB.auth.signOut()
                          var intent = Intent(this@FragmentClientSettings.context,LoginActivity::class.java)
                          startActivity(intent)
                      }
                      clientDepositButton.setOnClickListener {
                          if (doesUserHaveAccountID) {
                              if(client.client_isibkr_linked) {
                                  var builder =
                                      AlertDialog.Builder(this@FragmentClientSettings.requireContext())
                                  var customView = LayoutInflater.from(requireContext())
                                      .inflate(R.layout.dialog_invoice_deposit, null)
                                  builder.setView(customView)
                                  var createdBuilder = builder.create()
                                  //If user clicks cancel
                                  customView.findViewById<Button>(R.id.invoice_cancel_btn)
                                      .setOnClickListener {
                                          lifecycleScope.launch {
//                                          // Just a test but start an activity
                                              Toast.makeText(
                                                  requireContext(),
                                                  "Clicked dismiss!",
                                                  Toast.LENGTH_LONG
                                              )
                                                  .show()
                                              delay(5000)
                                              var intent = Intent(requireContext(),ActivityVerifyDepositAccount::class.java)
                                              startActivity(intent)

//                                          createdBuilder.dismiss()
                                          }

                                      }
                                  //User deposit inside of deposit class
                                  customView.findViewById<Button>(R.id.invoice_deposit_btn).setOnClickListener {

                                  }

                                  //If user clicks
                                  createdBuilder.show()

                                  //Create transaction and save paymentIntent on firebase.
                                  val url =
                                      URL("https://us-central1-latte-d25b7.cloudfunctions.net/createInvoice?stripeid=" + client.client_account_number + "&amount=5&env=prod")
                                  Log.d("FragmentClientSettings", "Payment URL: " + url)
                                  Toast.makeText(context, "Deposited!", Toast.LENGTH_LONG).show()
                                  lifecycleScope.launch {
//                                      var deposit = stripeAPI.createInvoice(
//                                          client.client_account_number,
//                                          5,
//                                          "prod"
//                                      )
//                                      Log.d("FragmentClientSettings: ", deposit.toString())
                                  }
                              }
                              //Else client checking account is not verified, so take them to verification page
                              else {
//                                  Toast.makeText(requireContext(),"Account not verified yet.",Toast.LENGTH_LONG).show()
                              //Deposit is not confimed, start deposit activity
                                var intent = Intent(this@FragmentClientSettings.requireContext(),ActivityVerifyDepositAccount::class.java)
                                //Add extras
                                  //Routing Number
                                  //Bank account number
                                  //Confirm Amount 1
                                  //Confirm Amount 2
                                  intent.putExtra("email",client.client_email)

                                  startActivity(intent)
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
                          if (doesUserHaveAccountID) {
                              if(client.client_isibkr_linked){
                              var builder =
                                  AlertDialog.Builder(this@FragmentClientSettings.requireContext())
                              var customView = LayoutInflater.from(requireContext())
                                  .inflate(R.layout.dialog_client_withdraw, null)
                              builder.setView(customView)
                              var createdBuilder = builder.create()
                              var withdrawlAmount = customView.findViewById<EditText>(R.id.et_invoice_amount_withdrawal)
                              customView.findViewById<Button>(R.id.withdrawal_cancel_btn)
                                  .setOnClickListener {
                                      Toast.makeText(requireContext(), "Dismiss", Toast.LENGTH_LONG)
                                          .show()
                                      createdBuilder.dismiss()
                                  }
                              customView.findViewById<Button>(R.id.invoice_withdrawal_btn).setOnClickListener {
                                  var amount = Float(client.client_balance)
                                  var balance = Float.parseFloat(withdrawlAmount.text!!.toString())
                                  if(amount > balance) {
                                      Toast.makeText(this@FragmentClientSettings.requireContext(),"Amount to withdraw is more then holdings available.",Toast.LENGTH_LONG).show()
                                      }
                                  else if (amount <= balance){
                                      //Make withdrawal request
                                      lifecycleScope.launch {
                                          var withdrawal = stripeAPI.createWithdrawal(client.client_account_number, balance)
                                          Toast.makeText(this@FragmentClientSettings.requireContext(),"Your withdrawal request has been sent!.",Toast.LENGTH_LONG).show()

                                      }

                                  }

                              }
                              createdBuilder.show()


                             }   //else client has not verified IBKR Checking account
                              else {
                              Toast.makeText(requireContext(),"Cannot withdraw until deposit account is verified.",Toast.LENGTH_LONG).show()
                                }
                          }
                           //Else user does not have IBKR Client ID
                           else {
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