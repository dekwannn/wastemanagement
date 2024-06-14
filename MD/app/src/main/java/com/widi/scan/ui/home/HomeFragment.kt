package com.widi.scan.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            loadUserDataFromFirestore(userId)
        }

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

    @SuppressLint("RestrictedApi")
    private fun loadUserDataFromFirestore(userId: String?) {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userId ?: "")

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username")
                    binding?.tvUsername?.text = username
                } else {
                    Log.d(FragmentManager.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(FragmentManager.TAG, "get failed with ", exception)
            }
    }


}