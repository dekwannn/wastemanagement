package com.widi.scan.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.widi.scan.R
import com.widi.scan.ui.main.ViewModelFactory
import com.widi.scan.data.Result
import com.widi.scan.databinding.FragmentLoginBinding
import com.widi.scan.ui.auth.signup.SignUpActivity
import com.widi.scan.ui.home.HomeActivity
import com.widi.scan.ui.settings.SettingsFragmentDirections
import com.widi.scan.ui.utils.safeNavigate

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }

            btnLogin.setOnClickListener {
                if (edLoginEmail.text.isNotEmpty() && edLoginPassword.text?.length!! >= 8) {
                    loginViewModel.submitLogin(
                        email = edLoginEmail.text.toString(),
                        password = edLoginPassword.text.toString()
                    )
                } else {
                    Toast.makeText(requireActivity(), "Please fill the form correctly", Toast.LENGTH_SHORT).show()
                }
            }

            btnGoogle.setOnClickListener {
                // Google login logic
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            builder.setView(R.layout.loading)
            val dialog: AlertDialog = builder.create()

            loginViewModel.responseResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Result.Loading -> dialog.show()
                    is Result.Error -> {
                        dialog.dismiss()
                        Toast.makeText(requireActivity(), response.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        dialog.dismiss()
                        findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }
                    else -> dialog.dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
