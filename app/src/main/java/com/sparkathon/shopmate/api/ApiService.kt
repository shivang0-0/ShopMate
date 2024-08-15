package com.sparkathon.shopmate.api

import AuthResponse
import Category
import LoginRequest
import Product
import Profile
import RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @GET("/auth/profile")
    fun getProfile(@Query("email") email: String): Call<List<Profile>>

    @GET("/products")
    fun getProducts(
        @Query("category") category: String? = null,
        @Query("product_id") id: Int? = null
    ): Call<List<Product>>

    @GET("/products/categories")
    fun getCategories(): Call<List<Category>>
}
