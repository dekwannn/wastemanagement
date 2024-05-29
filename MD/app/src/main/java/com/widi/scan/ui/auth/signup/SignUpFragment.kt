package com.widi.scan.ui.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
        val name = binding?.edRegisterName?.text.toString()
        val email = binding?.edRegisterEmail?.text.toString()
        val password = binding?.edRegisterPassword?.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                findNavController().safeNavigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            } else {
                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}
