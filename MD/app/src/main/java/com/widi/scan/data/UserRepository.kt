package com.widi.scan.data

import com.widi.scan.data.pref.UserPreference
import com.widi.scan.data.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference,
) {
    fun getToken() = pref.getToken()

    suspend fun saveToken(token: String) = pref.saveToken(token)

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, pref: UserPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref).also { instance = it }
            }
    }
}