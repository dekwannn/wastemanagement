package com.widi.scan.data

import com.widi.scan.data.pref.UserPreference
import com.widi.scan.data.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference,
) {

}