package com.widi.scan.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.widi.scan.R
import com.widi.scan.data.pref.UserPreference
import com.widi.scan.ui.utils.safeNavigate

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
                    findNavController().safeNavigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
                }
                userPreferences.isOnboardingComplete() -> {
                    findNavController().safeNavigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
                }
                else -> {
                    findNavController().safeNavigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToOnBoardingFragment())
                }
            }
        }, SPLASH_TIME_OUT)
    }


    companion object {
        const val SPLASH_TIME_OUT = 3000L
    }
}
