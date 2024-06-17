package com.widi.scan.ui.onboarding

import OnBoardingViewPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.widi.scan.R
import com.widi.scan.data.pref.UserPreference
import com.widi.scan.databinding.FragmentOnBoardingBinding
import com.widi.scan.data.model.OnBoarding
import com.widi.scan.ui.utils.safeNavigate

class OnBoardingFragment : Fragment() {

    private lateinit var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter
    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var userPreferences: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreference(requireContext())

        val onBoardingData: MutableList<OnBoarding> = ArrayList()
        onBoardingData.add(
            OnBoarding("Welcome to SCAN", "Find and identify types of waste easily and accurately through image scanning",
            R.drawable.onboarding1
        )
        )
        onBoardingData.add(
            OnBoarding("Scan Your Waste", "Take photos of your trash to get recycling information and recommendations.",
            R.drawable.onboarding2
        )
        )
        onBoardingData.add(
            OnBoarding("Get Recommendation", "Use the map to find the nearest waste collection points and recycling facilities near you.",
            R.drawable.onboarding3
        )
        )

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

        binding.apply {
            btnLogin.setOnClickListener {
                userPreferences.setOnboardingComplete(true)
                findNavController().safeNavigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment())
            }
            btnRegister.setOnClickListener {
                userPreferences.setOnboardingComplete(true)
                findNavController().safeNavigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToSignUpFragment())
            }

        }




    }
}
