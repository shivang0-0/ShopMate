package com.sparkathon.shopmate.preferences

import android.content.Context
import android.content.SharedPreferences

private const val USER_PREFS = "UserPreferences"
private const val USER_EMAIL_KEY = "user_email"

private fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
}

fun saveEmailToPreferences(context: Context, email: String) {
    val sharedPreferences = getSharedPreferences(context)
    sharedPreferences.edit().apply {
        putString(USER_EMAIL_KEY, email)
        apply()
    }
}

fun getEmailFromPreferences(context: Context): String? {
    val sharedPreferences = getSharedPreferences(context)
    return sharedPreferences.getString(USER_EMAIL_KEY, null)
}
