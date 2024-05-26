package com.widi.scan.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.widi.scan.R
import com.widi.scan.databinding.ActivityHomeBinding
import com.widi.scan.databinding.ActivitySignUpBinding
import com.widi.scan.ui.camera.CameraActivity
import com.widi.scan.ui.scan.ScanActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnScan.setOnClickListener {
                startScan()
            }
            btnCamera.setOnClickListener{
                startScan()
            }
            btnImage.setOnClickListener{
                startScan()
            }
            btnArticles.setOnClickListener {

            }
            btnHistory.setOnClickListener{

            }

        }
    }

    private fun startScan() {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }

}