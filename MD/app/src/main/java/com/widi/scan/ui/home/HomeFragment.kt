package com.widi.scan.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.widi.scan.R
import com.widi.scan.databinding.FragmentHomeBinding
import com.widi.scan.ui.utils.safeNavigate


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

        binding?.relativeLayout?.startAnimation(fadeIn)
        binding?.scrollView?.startAnimation(fadeIn)

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
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

}