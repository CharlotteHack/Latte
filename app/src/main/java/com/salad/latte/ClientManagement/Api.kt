package com.salad.latte.ClientManagement

import retrofit2.Call
import retrofit2.http.GET

// In Api.kt file that you create
interface Api {

    @GET("/client_token")
    fun getClientToken(): Call<ClientToken>
}