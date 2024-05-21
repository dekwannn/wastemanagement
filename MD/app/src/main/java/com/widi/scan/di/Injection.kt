package com.widi.scan.di

import android.content.Context
import com.widi.scan.data.UserRepository
import com.widi.scan.data.pref.UserPreference
import com.widi.scan.data.pref.dataStore
import com.widi.scan.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(apiService, pref)
    }
}