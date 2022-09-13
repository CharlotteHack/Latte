package com.salad.latte.ClientManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
//import com.braintreepayments.api.BraintreeClient
//import com.braintreepayments.api.ClientTokenCallback
//import com.braintreepayments.api.ClientTokenProvider
import com.salad.latte.R
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory

class FragmentClientSettings : Fragment() {
    lateinit var depositBtn :Button
//    private lateinit var braintreeClient: BraintreeClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_client_settings,container,false)
        depositBtn = v.findViewById(R.id.client_deposit_button)
//        braintreeClient = BraintreeClient(requireContext(), "sandbox_tvrwq73x_2rd4b67txtr9drx2")
//        braintreeClient = BraintreeClient(requireContext(), ExampleClientTokenProvider())

        depositBtn.setOnClickListener {


        }
        return v;
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