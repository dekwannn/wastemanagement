package com.widi.scan.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.widi.scan.R
import com.widi.scan.ui.main.ViewModelFactory
import com.widi.scan.data.Result
import com.widi.scan.databinding.ActivityLoginBinding
import com.widi.scan.ui.auth.signup.SignUpActivity
import com.widi.scan.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val loginViewModel: LoginViewModel by viewModels<LoginViewModel> {
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME")

        binding.apply {
            tvRegister.setOnClickListener{
                val registerActivity = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(registerActivity)
            }

            btnLogin.setOnClickListener {

                if(edLoginEmail.text.isNotEmpty() && edLoginPassword.text?.length!! >= 8 ){
                    loginViewModel.submitLogin(
                        email = edLoginEmail.text.toString(),
                        password = edLoginPassword.text.toString()
                    )
                }else{
                    Toast.makeText(this@LoginActivity, "Please fill the form correctly", Toast.LENGTH_SHORT).show()
                }
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
            builder.setView(R.layout.loading)
            val dialog: AlertDialog = builder.create()

            loginViewModel.responseResult.observe(this@LoginActivity) { response ->
                when (response) {
                    is Result.Loading -> dialog.show()
                    is Result.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Success -> {
                        dialog.dismiss()
                        val homeActivity = Intent(this@LoginActivity, MainActivity::class.java)
                        homeActivity.putExtra("USERNAME", username)
                        startActivity(homeActivity)
                        finish()
                    }

                    else -> dialog.dismiss()
                }
            }
        }
    }

}