package com.sparkathon.shopmate.preferences

import android.content.Context
import android.content.SharedPreferences

fun saveEmailToPreferences(context: Context, email: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putString("user_email", email)
    editor.apply()
}

fun getEmailFromPreferences(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getString("user_email", null)
}
