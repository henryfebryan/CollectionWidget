package com.example.collectionwidget.data

import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.ArrayList

interface ApiService {
    @Headers("Content-Type: application/json", "x-api-key: cb1f4008-8617-4029-9e70-4311857f637f")
    @POST("coins/list")
    fun coinlist(@Body body: RequestCoinBody): Call<ArrayList<Coin>>

    companion object {
        private const val ENDPOINT = "https://api.livecoinwatch.com/"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}