package com.widi.scan.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager.TAG
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.widi.scan.data.ScanRepository
import com.widi.scan.databinding.FragmentSettingsBinding
import com.widi.scan.ui.main.ViewModelFactory
import com.widi.scan.ui.utils.safeNavigate

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        ViewModelFactory(ScanRepository())
    }

    private val settingsViewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            loadUserDataFromFirestore(userId)
        }
        val currentUser = auth.currentUser
        val userEmail = currentUser?.email ?: "Unknown"

        binding?.email?.text = userEmail

        binding?.btnLogout?.setOnClickListener {
            logout()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun loadUserDataFromFirestore(userId: String?) {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userId ?: "")

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username")
                    binding?.userName?.text = username
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun logout() {

        auth.signOut()
        settingsViewModel.clearHistory()
        findNavController().safeNavigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
    }

}