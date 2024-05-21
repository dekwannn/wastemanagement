package com.widi.scan.ui.onboarding

import OnBoardingViewPagerAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.widi.scan.R
import com.widi.scan.databinding.ActivityOnBoardingBinding
import com.widi.scan.ui.auth.login.LoginActivity
import com.widi.scan.ui.auth.signup.SignUpActivity
import com.widi.scan.model.OnBoarding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter
    private lateinit var onBoardingViewPager: ViewPager2
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBoardingViewPager = findViewById(R.id.screenPager)

        val onBoardingData: MutableList<OnBoarding> = ArrayList()
        onBoardingData.add(OnBoarding("Welcome to SCAN", "Find and identify types of waste easily and accurately through image scanning",
            R.drawable.onboarding1
        ))
        onBoardingData.add(OnBoarding("Scan Your Waste", "Find and identify types of waste easily and accurately through image scanning",
            R.drawable.onboarding2
        ))
        onBoardingData.add(OnBoarding("Get Recommendation", "Find and identify types of waste easily and accurately through image scanning",
            R.drawable.onboarding3
        ))

        val wormDotsIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager.adapter = onBoardingViewPagerAdapter
        wormDotsIndicator.attachTo(onBoardingViewPager)

        var position = onBoardingViewPager.currentItem
        binding.btnNext.setOnClickListener{
            if(position < onBoardingData.size - 1){
                position++
                onBoardingViewPager.currentItem = position
            }
        }

        binding.btnPrev.setOnClickListener{
            if(position > 0){
                position--
                onBoardingViewPager.currentItem = position
            }
        }



        onBoardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onBoardingData.size - 1) {
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.btnRegister.visibility = View.VISIBLE
                } else {
                    binding.btnLogin.visibility = View.GONE
                    binding.btnRegister.visibility = View.GONE
                }
            }
        })

        binding.btnLogin.setOnClickListener {
            val sharedPref = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isFinished", true)
            editor.apply()

            val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val sharedPref = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isFinished", true)
            editor.apply()

            val intent = Intent(this@OnBoardingActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

