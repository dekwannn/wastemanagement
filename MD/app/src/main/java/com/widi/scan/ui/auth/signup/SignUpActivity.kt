package com.widi.scan.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.widi.scan.R
import com.widi.scan.ui.main.ViewModelFactory
import com.widi.scan.data.Result
import com.widi.scan.databinding.ActivitySignUpBinding
import com.widi.scan.ui.auth.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val signupViewModel:SignUpViewModel by viewModels<SignUpViewModel>{
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginActivity = Intent(this, LoginActivity::class.java)
        binding.apply {
            tvLogin.setOnClickListener{
                startActivity(loginActivity)
            }

            btnRegister.setOnClickListener {
                if (edRegisterEmail.text.isNotEmpty() && edRegisterName.text.isNotEmpty() && edRegisterPassword.text?.length!! >= 8){
                    signupViewModel.submitRegister(
                        name = edRegisterName.text.toString(),
                        email = edRegisterEmail.text.toString(),
                        password = edRegisterPassword.text.toString()
                    )
                }else{
                    Toast.makeText(this@SignUpActivity, "Please fill the form correctly", Toast.LENGTH_SHORT).show()
                }

            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@SignUpActivity)
            builder.setView(R.layout.loading)
            val dialog: AlertDialog = builder.create()

            signupViewModel.responseResult.observe(this@SignUpActivity) {
                    response ->
                when (response) {
                    is Result.Loading -> dialog.show()
                    is Result.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@SignUpActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Success -> {
                        dialog.dismiss()
                        val loginActivity = Intent(this@SignUpActivity, LoginActivity::class.java)
                        loginActivity.putExtra("USERNAME", edRegisterName.text.toString())
                        startActivity(loginActivity)
                        finish()
                    }

                    else -> dialog.dismiss()
                }
            }
        }
    }
}