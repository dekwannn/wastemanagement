package com.widi.scan.data.pref

import android.content.Context
import android.content.SharedPreferences

class UserPreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    }

    fun setOnboardingComplete(isComplete: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETE, isComplete).apply()
    }

    fun isOnboardingComplete(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETE, false)
    }

}