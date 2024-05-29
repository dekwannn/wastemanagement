package com.widi.scan.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.widi.scan.R
import com.widi.scan.data.pref.UserPreference

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private lateinit var userPreferences: UserPreference
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreference(requireContext())
        auth = FirebaseAuth.getInstance()

        view.postDelayed({
            when {
                auth.currentUser != null -> {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                }
                userPreferences.isOnboardingComplete() -> {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                }
                else -> {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                }
            }
        }, SPLASH_TIME_OUT)
    }


    companion object {
        const val SPLASH_TIME_OUT = 3000L
    }
}
