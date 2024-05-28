package com.widi.scan.ui.auth.signup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.widi.scan.R
import com.widi.scan.data.Result
import com.widi.scan.databinding.FragmentSignUpBinding
import com.widi.scan.ui.auth.login.LoginFragment
import com.widi.scan.ui.main.ViewModelFactory

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val signupViewModel: SignUpViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }

            btnRegister.setOnClickListener {
                if (edRegisterEmail.text.isNotEmpty() && edRegisterName.text.isNotEmpty() && edRegisterPassword.text?.length!! >= 8) {
                    signupViewModel.submitRegister(
                        name = edRegisterName.text.toString(),
                        email = edRegisterEmail.text.toString(),
                        password = edRegisterPassword.text.toString()
                    )
                } else {
                    Toast.makeText(requireContext(), "Please fill the form correctly", Toast.LENGTH_SHORT).show()
                }
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setView(R.layout.loading)
            val dialog: AlertDialog = builder.create()

            signupViewModel.responseResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Result.Loading -> dialog.show()
                    is Result.Error -> {
                        dialog.dismiss()
                        Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Success -> {
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment, Bundle().apply {
                        })
                    }

                    else -> dialog.dismiss()
                }
            }
        }
    }
}
