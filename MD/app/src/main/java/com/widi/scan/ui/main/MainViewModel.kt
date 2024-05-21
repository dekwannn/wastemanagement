package com.widi.scan.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.widi.scan.data.pref.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: UserPreference
): ViewModel(){
    fun getToken() = pref.getToken().asLiveData()

    fun setOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            pref.setOnboardingCompleted(completed)
        }
    }

    fun isOnboardingCompleted(): LiveData<Boolean> {
        return pref.isOnboardingCompleted().asLiveData()
    }

    fun getThemeSetting() = pref.getThemeSetting().asLiveData()

    fun logout(){
        viewModelScope.launch {
            pref.clearToken()
        }
    }

}