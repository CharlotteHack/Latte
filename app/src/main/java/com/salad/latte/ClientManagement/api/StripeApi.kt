package com.salad.latte.ClientManagement.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

//var stripeKey = "U4693995"
interface StripeApi  {
    @GET("createACHDeposit")
    suspend fun createDeposit(@Query("stripeid") key :String) : String



}