package com.sparkathon.shopmate.api

import LoginRequest
import RegisterRequest
import com.sparkathon.shopmate.auth.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>
}
