package com.sparkathon.shopmate.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val WISHLIST_PREFS = "WishlistPreferences"
private const val WISHLIST_ITEMS_KEY = "wishlist_items"

private fun getWishlistSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(WISHLIST_PREFS, Context.MODE_PRIVATE)
}

private fun getWishlistItemsSet(context: Context): MutableSet<Int> {
    val sharedPreferences = getWishlistSharedPreferences(context)
    val wishlistJson = sharedPreferences.getString(WISHLIST_ITEMS_KEY, null)
    return if (wishlistJson != null) {
        val type = object : TypeToken<MutableSet<Int>>() {}.type
        Gson().fromJson(wishlistJson, type)
    } else {
        mutableSetOf()
    }
}

fun addToWishlist(context: Context, productId: Int) {
    val wishlistItems = getWishlistItemsSet(context)
    wishlistItems.add(productId)
    saveWishlistItems(context, wishlistItems)
}

fun getWishlistItems(context: Context): Set<Int> {
    return getWishlistItemsSet(context)
}

fun saveWishlistItems(context: Context, wishlistItems: Set<Int>) {
    val sharedPreferences = getWishlistSharedPreferences(context)
    val editor = sharedPreferences.edit()
    val wishlistJson = Gson().toJson(wishlistItems)
    editor.putString(WISHLIST_ITEMS_KEY, wishlistJson)
    editor.apply()
}

fun removeFromWishlist(context: Context, productId: Int) {
    val wishlistItems = getWishlistItemsSet(context)
    wishlistItems.remove(productId)
    saveWishlistItems(context, wishlistItems)
}
