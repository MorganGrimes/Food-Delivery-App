package com.example.fooddeliveryapp.utils

import android.content.Context
import android.content.SharedPreferences

object ProfileSharedPreferences {

    private const val PREF_NAME = "food_prefs"
    private const val KEY_ONBOARDING_COMPLETED = "completed"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isOnboardingCompleted(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setOnboardingCompleted(context: Context, completed: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
}