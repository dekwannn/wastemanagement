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
import com.widi.scan.R

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val mainViewModel: MainViewModel by viewModels {
        factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
                    if (token != null) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                    } else {
                        mainViewModel.isOnboardingCompleted().observe(viewLifecycleOwner) { onboardingCompleted ->
                            if (!onboardingCompleted) {
                                findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                            } else {
                                findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                            }
                        }
                    }
                }
            },
            SPLASH_TIME_OUT
        )
    }


    companion object {
        const val SPLASH_TIME_OUT = 3000L
    }
}
