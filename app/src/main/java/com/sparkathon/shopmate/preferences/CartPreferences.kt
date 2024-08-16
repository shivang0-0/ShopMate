package com.sparkathon.shopmate.preferences

import Product
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import showToast

private const val CART_PREFS = "CartPreferences"
private const val CART_ITEMS_KEY = "cart_items"

private fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
}

private fun getCartItemsMap(context: Context): MutableMap<String, Int> {
    val sharedPreferences = getSharedPreferences(context)
    val cartJson = sharedPreferences.getString(CART_ITEMS_KEY, null)
    return if (cartJson != null) {
        val type = object : TypeToken<MutableMap<String, Int>>() {}.type
        Gson().fromJson(cartJson, type)
    } else {
        mutableMapOf()
    }
}

fun addToCart(context: Context, productId: String) {
    val cartItems = getCartItemsMap(context)

    cartItems[productId] = cartItems.getOrDefault(productId, 0) + 1

    saveCartItems(context, cartItems)
}

fun getCartItems(context: Context): Map<String, Int> {
    return getCartItemsMap(context)
}

fun saveCartItems(context: Context, cartItems: Map<String, Int>) {
    val sharedPreferences = getSharedPreferences(context)
    val editor = sharedPreferences.edit()
    val cartJson = Gson().toJson(cartItems)
    editor.putString(CART_ITEMS_KEY, cartJson)
    editor.apply()
}

fun removeProductFromCart(context: Context, productId: String) {
    val cartItems = getCartItemsMap(context)
    cartItems.remove(productId)
    saveCartItems(context, cartItems)
}

fun updateProductQuantity(context: Context, productId: String, quantity: Int) {
    val cartItems = getCartItemsMap(context)
    if (quantity > 0) {
        cartItems[productId] = quantity
    } else {
        cartItems.remove(productId)
    }
    saveCartItems(context, cartItems)
}

fun clearCart(context: Context) {
    val sharedPreferences = getSharedPreferences(context)
    val editor = sharedPreferences.edit()
    editor.remove(CART_ITEMS_KEY)
    editor.apply()
}
