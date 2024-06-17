package com.widi.scan.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.widi.scan.R
import com.widi.scan.data.ScanRepository
import com.widi.scan.databinding.FragmentHomeBinding
import com.widi.scan.ui.main.ViewModelFactory
import com.widi.scan.ui.utils.safeNavigate


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var homeAdapter: HomeAdapter
    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(ScanRepository())
    }
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }
    @SuppressLint("NotifyDataSetChanged")
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
                findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToScanFragment())
            }
            btnLocation.setOnClickListener{
                findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToMapsFragment())
            }
            btnArticles.setOnClickListener {
                 findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToArticleFragment2())
            }
            btnHistory.setOnClickListener {
                findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToHistoryFragment())
            }
            btnCategory.setOnClickListener {
                findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToArticleFragment2())
            }
        }

        homeAdapter = HomeAdapter(emptyList())

        binding?.rvArticles?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvArticles?.adapter = homeAdapter

        homeViewModel.getArticles().observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                homeAdapter.articles = articles
                homeAdapter.notifyDataSetChanged()
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
                }
            }
            .addOnFailureListener { exception ->
                Log.d(FragmentManager.TAG, "get failed with ", exception)
            }
    }


}