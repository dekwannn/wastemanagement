package com.widi.scan.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.widi.scan.R
import com.widi.scan.databinding.ActivityScanBinding
import com.widi.scan.ui.camera.CameraActivity
import com.widi.scan.ui.camera.CameraActivity.Companion.CAMERAX_RESULT

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.apply {
            buttonGallery.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            buttonResult.setOnClickListener {
                if (imagePreview.toString().isEmpty()) {
                    showToast(getString(R.string.empty_image_warning))
                    return@setOnClickListener
                }

            }
        }
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                currentImageUri = it
                showImage()
            } else {
                showToast(getString(R.string.failed_to_pick_image))
            }
        }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imagePreview.setImageURI(it)
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERA_IMAGE)?.toUri()
            showImage()
        }
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}