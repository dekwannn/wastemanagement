package com.widi.scan.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.widi.scan.data.UserRepository
import com.widi.scan.data.pref.UserPreference
import com.widi.scan.data.pref.dataStore
import com.widi.scan.di.Injection
import com.widi.scan.ui.auth.login.LoginViewModel
import com.widi.scan.ui.auth.signup.SignUpViewModel

class ViewModelFactory private constructor(
    private val storyRepository: UserRepository,
    private val pref: UserPreference
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref = UserPreference.getInstance(context.dataStore))
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }else if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(storyRepository) as T
        }else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}