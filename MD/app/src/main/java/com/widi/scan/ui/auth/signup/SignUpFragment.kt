package com.widi.scan.ui.auth.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.TAG
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.widi.scan.R
import com.widi.scan.databinding.FragmentSignUpBinding
import com.widi.scan.ui.utils.safeNavigate

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding?.main?.startAnimation(fadeIn)
        auth = Firebase.auth

        binding?.apply {
            btnRegister.setOnClickListener {
                firebaseRegister(binding)
            }
            tvLogin.setOnClickListener {
                findNavController().safeNavigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            }
        }
    }

    private fun firebaseRegister(binding: FragmentSignUpBinding?) {
        val username = binding?.edRegisterName?.text.toString()
        val email = binding?.edRegisterEmail?.text.toString()
        val password = binding?.edRegisterPassword?.text.toString()

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveUserDataToFirestore(task.result?.user?.uid, username)
                findNavController().safeNavigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            } else {
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun saveUserDataToFirestore(userId: String?, username: String) {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userId ?: "")
        val userData = hashMapOf(
            "username" to username
        )

        userRef.set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
            }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}
