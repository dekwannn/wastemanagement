package com.widi.scan.ui.home

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.widi.scan.R
import com.widi.scan.databinding.FragmentHomeBinding
import com.widi.scan.ui.settings.SettingsFragmentDirections
import com.widi.scan.ui.utils.safeNavigate


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val username = arguments?.getString("username")
        binding?.tvUsername?.text = username

        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)

        binding?.relativeLayout?.startAnimation(fadeIn)
        binding?.scrollView?.startAnimation(slideUp)

        binding?.apply {
            btnScan.setOnClickListener {
                findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToScanFragment())
            }
            btnCamera.setOnClickListener {
                findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToCameraFragment())
            }
            btnArticles.setOnClickListener {
                 findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToArticleFragment2())
            }
            btnHistory.setOnClickListener {
                findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToHistoryFragment())
            }
            btnSeeall.setOnClickListener {
                logout()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun logout() {
        auth.signOut()
        findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
    }

}