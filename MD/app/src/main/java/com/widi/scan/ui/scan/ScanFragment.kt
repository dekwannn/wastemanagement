package com.widi.scan.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.widi.scan.R
import com.widi.scan.databinding.FragmentScanBinding
import com.widi.scan.ui.camera.CameraActivity
import com.widi.scan.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.widi.scan.ui.camera.CameraFragment
import com.widi.scan.ui.settings.SettingsFragmentDirections
import com.widi.scan.ui.utils.safeNavigate

class ScanFragment : Fragment(R.layout.fragment_scan) {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScanBinding.bind(view)

        binding.apply {
            buttonGallery.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            buttonResult.setOnClickListener {
                if (imagePreview.toString().isEmpty()) {
                    showToast(getString(R.string.empty_image_warning))
                    return@setOnClickListener
                }
                // Implement your result logic here
            }
        }
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                showToast(getString(R.string.failed_to_pick_image))
            }
        }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imagePreview.setImageURI(it)
        }
    }

    private fun startCamera() {
        findNavController().safeNavigate(
            ScanFragmentDirections.actionScanFragmentToCameraFragment())
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val data = result.data
            if (data != null) {
                currentImageUri = data.getStringExtra(CameraActivity.EXTRA_CAMERA_IMAGE)?.toUri()
                showImage()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
