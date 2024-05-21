package com.widi.scan.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.widi.scan.R
import com.widi.scan.ui.auth.login.LoginActivity
import com.widi.scan.ui.onboarding.OnBoardingActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels<MainViewModel> {
            factory
        }

        Handler(Looper.getMainLooper()).postDelayed(
            {
                mainViewModel.getThemeSetting().observe(this){
                        isDarkModeActive ->
                    AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                }

                mainViewModel.isOnboardingCompleted().observe(this) { onboardingCompleted ->
                    val intent = if (!onboardingCompleted) {
                        Intent(this, OnBoardingActivity::class.java)
                    } else {
                        Intent(this, LoginActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }

                mainViewModel.getToken().observe(this){
                        token ->
                    val intentActivity = Intent(this, if (token == null) OnBoardingActivity::class.java else MainActivity::class.java)
                    startActivity(intentActivity)
                    finish()
                }

            },
            SPLASH_TIME_OUT
        )

    }

    companion object {
        const val SPLASH_TIME_OUT = 3000L
    }
}