package com.sparkathon.shopmate.auth

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/login")
    fun login(@Body email: String, @Body password: String): Call<AuthResponse>

    @POST("/register")
    fun register(@Body email: String, @Body password: String): Call<AuthResponse>
}
