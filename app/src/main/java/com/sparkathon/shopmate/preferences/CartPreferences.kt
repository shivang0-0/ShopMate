package com.sparkathon.shopmate.preferences

import Product
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import showToast

fun addToCart(context: Context, product: Product) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("CartPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()

    // Retrieve the cart items as a JSON string
    val cartJson = sharedPreferences.getString("cart_items", null)
    val cartItems: MutableMap<String, Int> = if (cartJson != null) {
        // Convert the JSON string back to a Map
        val type = object : TypeToken<MutableMap<String, Int>>() {}.type
        gson.fromJson(cartJson, type)
    } else {
        mutableMapOf()
    }

    // Check if the product ID is already in the cart
    val productId = product.id.toString()
    if (cartItems.containsKey(productId)) {
        // Increase the quantity
        cartItems[productId] = cartItems[productId]!! + 1
    } else {
        // Add the product with a quantity of 1
        cartItems[productId] = 1
    }

    // Save the updated cart back to SharedPreferences
    val updatedCartJson = gson.toJson(cartItems)
    editor.putString("cart_items", updatedCartJson)
    editor.apply()

    // Show the toast message
    showToast(context, "Added ${product.title} to cart (Quantity: ${cartItems[productId]})")
}

fun getCartItems(context: Context): Map<String, Int> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("CartPreferences", Context.MODE_PRIVATE)
    val gson = Gson()

    // Retrieve the cart items as a JSON string
    val cartJson = sharedPreferences.getString("cart_items", null)
    Log.d("getCartItems", "Cart JSON: $cartJson")
    return if (cartJson != null) {
        // Convert the JSON string back to a Map
        val type = object : TypeToken<MutableMap<String, Int>>() {}.type
        gson.fromJson(cartJson, type)
    } else {
        // Return an empty map if no items are in the cart
        emptyMap()
    }
}
