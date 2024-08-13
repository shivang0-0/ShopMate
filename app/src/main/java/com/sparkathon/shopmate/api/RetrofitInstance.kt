package com.sparkathon.shopmate.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://143.110.182.65:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
