package com.widi.scan.ui.onboarding

import OnBoardingViewPagerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.widi.scan.R
import com.widi.scan.databinding.FragmentOnBoardingBinding
import com.widi.scan.model.OnBoarding
import com.widi.scan.ui.auth.login.LoginActivity
import com.widi.scan.ui.auth.signup.SignUpActivity

class OnBoardingFragment : Fragment() {

    private lateinit var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter
    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(requireContext(), onBoardingData)
        binding.screenPager.adapter = onBoardingViewPagerAdapter

        val wormDotsIndicator = binding.wormDotsIndicator
        wormDotsIndicator.attachTo(binding.screenPager)

        var position = binding.screenPager.currentItem
        binding.btnNext.setOnClickListener {
            if (position < onBoardingData.size - 1) {
                position++
                binding.screenPager.currentItem = position
            }
        }

        binding.btnPrev.setOnClickListener {
            if (position > 0) {
                position--
                binding.screenPager.currentItem = position
            }
        }

        binding.screenPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
            val sharedPref = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isFinished", true)
            editor.apply()

            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isFinished", true)
            editor.apply()

            findNavController().navigate(R.id.action_onBoardingFragment_to_signUpFragment)
        }
    }
}
