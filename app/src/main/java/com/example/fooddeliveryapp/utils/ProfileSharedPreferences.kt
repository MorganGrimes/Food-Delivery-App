package com.example.fooddeliveryapp.utils

import android.content.Context
import android.content.SharedPreferences

object ProfileSharedPreferences {

    private const val PREF_NAME = "food_prefs"
    private const val KEY_ONBOARDING_COMPLETED = "completed"
    private const val KEY_NAME = "user_name"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_PASSWORD = "user_password"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isOnboardingCompleted(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    fun saveUserData(context: Context, name: String, email: String, password: String) {
        getPreferences(context).edit().apply {
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    fun getUserName(context: Context): String? = getPreferences(context).getString(KEY_NAME, null)
    fun getUserEmail(context: Context): String? = getPreferences(context).getString(KEY_EMAIL, null)
    fun getUserPassword(context: Context): String? =
        getPreferences(context).getString(KEY_PASSWORD, null)
}