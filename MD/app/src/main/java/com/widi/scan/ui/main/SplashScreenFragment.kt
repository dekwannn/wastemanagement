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
import com.widi.scan.ui.home.HomeActivity
import com.widi.scan.ui.onboarding.OnBoardingActivity

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val mainViewModel: MainViewModel by viewModels {
        factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("SplashScreenFragment", "onViewCreated() called")

        Handler(Looper.getMainLooper()).postDelayed(
            {
                mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
                    Log.d("SplashScreenFragment", "getToken() observed")
                    if (token != null) {
                        Log.d("SplashScreenFragment", "Navigating to HomeFragment (token is not null)")
                        findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                    } else {
                        Log.d("SplashScreenFragment", "Token is null")
                        // Hanya navigasi ke OnBoardingFragment jika token null dan onboarding belum selesai
                        mainViewModel.isOnboardingCompleted().observe(viewLifecycleOwner) { onboardingCompleted ->
                            Log.d("SplashScreenFragment", "isOnboardingCompleted() observed")
                            if (!onboardingCompleted) {
                                Log.d("SplashScreenFragment", "Navigating to OnBoardingFragment")
                                findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                            } else {
                                Log.d("SplashScreenFragment", "OnBoarding already completed, navigating to HomeFragment")
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
