package com.salad.latte.ClientManagement

//import com.braintreepayments.api.BraintreeClient
//import com.braintreepayments.api.ClientTokenCallback
//import com.braintreepayments.api.ClientTokenProvider
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.salad.latte.ClientManagement.ViewModels.FragmentSettingsViewModel
import com.salad.latte.ClientManagement.api.StripeApi
import com.salad.latte.Database.FirebaseDB
import com.salad.latte.Objects.ClientTransaction
import com.salad.latte.R
import com.salad.latte.databinding.FragmentClientSettingsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.lang.Double
import java.lang.Float
import java.text.DecimalFormat
import kotlin.Any
import kotlin.Boolean
import kotlin.String
import kotlin.apply
import kotlin.toString

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
                  Log.d("FragmentClientSettings","Account: "+client.client_email)

                  binding.tvClientAccountbalanceSettings.setText("Net Account Value: $"+client.currencyFormat(client.client_balance))
                  binding.etClientFirstname.setText(client.client_name)
                  binding.tvClientEmailSettings.setText("Email: "+client.client_email)
                  doesUserHaveAccountID = !client.client_account_number.equals("none")
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
                          Log.d("FragmentClientSettings: 93","Button clicked. Does user have Account I?: "+doesUserHaveAccountID+" is user ibkr linked: "+client.client_isibkr_linked)
                          if (doesUserHaveAccountID) {
                              if(client.client_isibkr_linked) {
                                  //User is fully verified, show them the deposit dialog
                                  var builder =
                                      AlertDialog.Builder(this@FragmentClientSettings.requireContext())
                                  var customView = LayoutInflater.from(requireContext())
                                      .inflate(R.layout.dialog_invoice_deposit, null)
                                  builder.setView(customView)
                                  var createdBuilder = builder.create()
                                  var deposit_et = customView.findViewById<EditText>(R.id.et_invoice_amount)

                                  //If user clicks cancel
                                  customView.findViewById<Button>(R.id.invoice_cancel_btn)
                                      .setOnClickListener {
                                          lifecycleScope.launch {
//                                          // Just a test but start an activity
//                                              Toast.makeText(
//                                                  requireContext(),
//                                                  "Clicked dismiss!",
//                                                  Toast.LENGTH_LONG
//                                              )
//                                                  .show()

                                          createdBuilder.dismiss()
                                          }

                                      }
                                    Log.d("(118) FragmentClientSettings"," Bank #: "+client.client_bank_account_number)
                                  //Get Users Account Number
                                  customView.findViewById<TextView>(R.id.deposit_ban_tv).setText("Bank Acct. #: "+client.client_bank_account_number)
                                  //Get Users Routing Number
                                  customView.findViewById<TextView>(R.id.deposit_routing_tv).setText("Routing #: "+client.client_routing_number)



                                  //User deposit inside of deposit class
                                  customView.findViewById<Button>(R.id.invoice_deposit_btn).setOnClickListener {
                                        //Write the deposit request to firebase, if it succeeds then navigate to deposit success page
                                      lifecycleScope.launch {
                                          //Not only do u add to request Queue, but u also need to add to transactions

                                          if(isDepositValid(deposit_et.text.toString())){
//                                              delay(5000)
                                              //First add the item to list of transactions

                                              var email = FirebaseAuth.getInstance().currentUser!!.email.toString()
                                              var emailKey = email.replace(".", "|")
                                              var transactions = HashMap<String,ClientTransaction>()
                                              var ts = System.currentTimeMillis()
                                              var amount = Double.valueOf(deposit_et.text.toString())
                                              var status = "pending"
                                              var type = "deposit"
                                              var clientTransaction = ClientTransaction(type,amount,status,ts)
//                                              clientTransaction.amount = amount
//                                              clientTransaction.status = status
//                                              clientTransaction.type = type
//                                              clientTransaction.timestamp = ts
                                              transactions.put(ts.toString(),clientTransaction)
                                              firebaseDB.mDatabase.child("Clients").child(emailKey).child("transactions").updateChildren(
                                                  transactions as Map<String, Any>
                                              ).addOnSuccessListener {
                                                    //Successfully added transaction request to firebase, now i have to make request queue call
                                                  //Logic to add to RequestQueue
                                                  Toast.makeText(this@FragmentClientSettings.requireContext(),"Deposit is processing!",Toast.LENGTH_LONG).show()
                                                  customView.findViewById<Button>(R.id.invoice_deposit_btn).isClickable = false
                                                  customView.findViewById<Button>(R.id.invoice_deposit_btn).isEnabled = false
                                                  FirebaseMessaging.getInstance().token
                                                      .addOnCompleteListener(OnCompleteListener { task ->
                                                          if (!task.isSuccessful) {
                                                              Log.w(
                                                                  ContentValues.TAG,
                                                                  "Fetching FCM registration token failed",
                                                                  task.exception
                                                              )
                                                              return@OnCompleteListener
                                                          }
                                                          // Get new FCM registration token
                                                          val token = task.result
                                                          val key: String =
                                                              firebaseDB.mDatabase.push().getKey()!!
                                                          val hashy =
                                                              java.util.HashMap<String, String?>()
                                                          hashy["action"] = "Client requested deposit of "+amount.toString()+" . Implement transaction on IBKR and send push notification if successful"
                                                          hashy["email"] = email
                                                          hashy["token"] = token
                                                          firebaseDB.mDatabase.child("RequestQueue").child(key)
                                                              .setValue(hashy).addOnSuccessListener {
                                                                  //The request succeeded, now u can show user success page
                                                                  lifecycleScope.launch {
                                                                      delay(3000)
                                                                      var intent = Intent(this@FragmentClientSettings.requireContext(),ActivityDepositSuccessful::class.java)
                                                                      startActivity(intent)
                                                                  }
                                                              }.addOnFailureListener {
                                                                  Toast.makeText(requireContext(),"Failed to record deposit transaction. Please visit our discord",Toast.LENGTH_LONG).show()
                                                              }
                                                      })
                                              }.addOnFailureListener {
                                                  //Failed to add the tranaction to firebase. Print out why here
                                                  Toast.makeText(this@FragmentClientSettings.requireContext(),"Failed to make deposit. Please try again later",Toast.LENGTH_LONG).show()
                                              }

//                                              var intent = Intent(requireContext(),ActivityVerifyDepositAccount::class.java)
//                                              startActivity(intent)

                                          }

                                      }
                                  }
                                  //If user clicks
                                  createdBuilder.show()
                              }
                              //Else client checking account is not verified, so take them to verification page
                              else {
                              //Deposit is not confirmed, start deposit activity
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
//                                      Toast.makeText(requireContext(), "Dismiss", Toast.LENGTH_LONG)
//                                          .show()
                                      createdBuilder.dismiss()
                                  }
                                  //
                              //Set Users Account Balance
                              customView.findViewById<TextView>(R.id.tv_withdrawal_netvalue).setText("$"+client.currencyFormat(client.client_balance))
                              customView.findViewById<TextView>(R.id.withdraw_ban_tv).setText("Bank Acct. #: "+client.client_bank_account_number)
                              customView.findViewById<TextView>(R.id.withdraw_routing_tv).setText("Routing #: "+client.client_routing_number)
                              customView.findViewById<Button>(R.id.invoice_withdrawal_btn).setOnClickListener {
                                  if(withdrawlAmount.text!!.toString() == ""){
                                      Toast.makeText(this@FragmentClientSettings.requireContext(),"Withdraw field cannot be empty.",Toast.LENGTH_LONG).show()

                                  }
                                  else {
                                      var balance = Float.valueOf(client.client_balance)
                                      var amount = Float.parseFloat(withdrawlAmount.text!!.toString())
                                      if(amount > balance) {
                                          Toast.makeText(this@FragmentClientSettings.requireContext(),"Amount to withdraw is more then holdings available.",Toast.LENGTH_LONG).show()
                                          }
                                      else if (amount <= balance){
                                          //Make withdrawal request
                                          lifecycleScope.launch {
                                              //Add the withdrawal request to the transactions
                                              Toast.makeText(this@FragmentClientSettings.requireContext(),"Withdrawal is processing!",Toast.LENGTH_LONG).show()
                                              customView.findViewById<Button>(R.id.invoice_withdrawal_btn).isClickable = false
                                              customView.findViewById<Button>(R.id.invoice_withdrawal_btn).isEnabled = false
                                              var email = FirebaseAuth.getInstance().currentUser!!.email.toString()
                                              var emailKey = email.replace(".", "|")
                                              var transactions = HashMap<String,ClientTransaction>()
                                              var ts = System.currentTimeMillis()
                                              var amount = Double.valueOf(amount.toString())
                                              var status = "pending"
                                              var type = "withdrawal"
                                              var clientTransaction = ClientTransaction(type,amount,status,ts)
//                                              clientTransaction.amount = amount
//                                              clientTransaction.status = status
//                                              clientTransaction.type = type
//                                              clientTransaction.timestamp = ts
                                              transactions.put(ts.toString(),clientTransaction)
                                              firebaseDB.mDatabase.child("Clients").child(emailKey).child("transactions").updateChildren(
                                                  transactions as Map<String, Any>
                                              ).addOnSuccessListener {
                                                  //Successfully added transaction request to firebase, now i have to make request queue call
                                                  //Logic to add to RequestQueue
                                                  FirebaseMessaging.getInstance().token
                                                      .addOnCompleteListener(OnCompleteListener { task ->
                                                          if (!task.isSuccessful) {
                                                              Log.w(
                                                                  ContentValues.TAG,
                                                                  "Fetching FCM registration token failed",
                                                                  task.exception
                                                              )
                                                              return@OnCompleteListener
                                                          }
                                                          // Get new FCM registration token
                                                          val token = task.result
                                                          val key: String =
                                                              firebaseDB.mDatabase.push().getKey()!!
                                                          val hashy =
                                                              java.util.HashMap<String, String?>()
                                                          hashy["action"] = "Client requested withdrawal of "+amount.toString()+" . Implement transaction on IBKR and send push notification if successful"
                                                          hashy["email"] = email
                                                          hashy["token"] = token
                                                          firebaseDB.mDatabase.child("RequestQueue").child(key)
                                                              .setValue(hashy).addOnSuccessListener {
                                                                  //The request succeeded, now u can show user success page
                                                                  lifecycleScope.launch {
                                                                      delay(3000)
                                                                      var intent = Intent(this@FragmentClientSettings.requireContext(),ActivityWithdrawalSuccessful::class.java)
                                                                      startActivity(intent)
                                                                  }
                                                              }.addOnFailureListener {
                                                                  Toast.makeText(requireContext(),"Failed to record withdrawal transaction. Please visit our discord",Toast.LENGTH_LONG).show()
                                                              }
                                                      })
                                              }.addOnFailureListener {
                                                  //Failed to add the transaction to firebase. Print out why here
                                                  Toast.makeText(this@FragmentClientSettings.requireContext(),"Failed to make withdrawal. Please try again later",Toast.LENGTH_LONG).show()
                                              }

    //                                              var intent = Intent(requireContext(),ActivityVerifyDepositAccount::class.java)
    //                                              startActivity(intent)



                                          }

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

    fun isDepositValid(value :String) : Boolean{
        if(value == ""){
            Toast.makeText(this@FragmentClientSettings.requireContext(),"Deposit field cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        var sumOneDouble = value.toDoubleOrNull()

        if(sumOneDouble == null){
            Toast.makeText(this@FragmentClientSettings.requireContext(),"Please enter a valid number",Toast.LENGTH_LONG).show()
            return false

        }
        if(sumOneDouble < 1){
            Toast.makeText(this@FragmentClientSettings.requireContext(),"Please enter a value greater than $1.00",Toast.LENGTH_LONG).show()
            return false

        }
        return true

    }

    fun isWithdrawalValid(value :String, accountValue : kotlin.Double) : Boolean{
        if(value == ""){
            Toast.makeText(this@FragmentClientSettings.requireContext(),"Deposit field cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        var sumOneDouble = value.toDoubleOrNull()

        if(sumOneDouble == null){
            Toast.makeText(this@FragmentClientSettings.requireContext(),"Please enter a valid number",Toast.LENGTH_LONG).show()
            return false

        }
        if(sumOneDouble < 1){
            Toast.makeText(this@FragmentClientSettings.requireContext(),"Please enter a value greater than $1.00",Toast.LENGTH_LONG).show()
            return false

        }
        return true

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