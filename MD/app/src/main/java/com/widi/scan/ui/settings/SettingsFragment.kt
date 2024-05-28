package com.widi.scan.ui.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.widi.scan.R
import com.widi.scan.databinding.FragmentHomeBinding
import com.widi.scan.databinding.FragmentSettingsBinding
import com.widi.scan.ui.auth.login.LoginViewModel
import com.widi.scan.ui.main.MainViewModel
import com.widi.scan.ui.main.ViewModelFactory
import com.widi.scan.ui.settings.SettingsFragmentDirections
import com.widi.scan.ui.utils.safeNavigate

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val mainViewModel: MainViewModel by viewModels {
        factory
    }
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding?.apply {
            btnLogout.setOnClickListener{
                Log.d("LogoutButton", "Button clicked")
                mainViewModel.logout()
                findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
            }



        }
    }


}