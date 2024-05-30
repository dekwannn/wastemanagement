package com.widi.scan.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.widi.scan.R
import com.widi.scan.databinding.BottomSheetDialogBinding
import com.widi.scan.databinding.FragmentScanBinding
import com.widi.scan.ui.camera.CameraFragment
import com.widi.scan.ui.camera.CameraFragment.Companion.CAMERAX_RESULT
import com.widi.scan.ui.utils.safeNavigate
import java.io.InputStream

class ScanFragment : Fragment(R.layout.fragment_scan) {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null
    private lateinit var wasteModel: WasteClassification

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
        wasteModel = WasteClassification(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScanBinding.bind(view)

        binding.apply {
            buttonGallery.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            buttonResult.setOnClickListener {
                if (currentImageUri == null) {
                    showToast(getString(R.string.empty_image_warning))
                    return@setOnClickListener
                }
                classifyImage()
            }

            btnBack.setOnClickListener {
                findNavController().safeNavigate(ScanFragmentDirections.actionScanFragmentToHomeFragment())
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
        launcherIntentCamera
        findNavController().safeNavigate(
            ScanFragmentDirections.actionScanFragmentToCameraFragment())
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val data = result.data
            if (data != null) {
                currentImageUri = data.getStringExtra(CameraFragment.EXTRA_CAMERA_IMAGE)?.toUri()
                showImage()
            }
        }
    }

    private fun classifyImage() {
        currentImageUri?.let { uri ->
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val preprocessedImage = wasteModel.preprocessImage(bitmap)
            val result = wasteModel.classify(preprocessedImage)

            showBottomSheetDialog(result)
        }
    }

    private fun showBottomSheetDialog(result: FloatArray) {
        val binding = BottomSheetDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)

        val labels = listOf("BATTERY", "BIOLOGICAL", "CLOTHES", "CARDBOARD", "GLASS", "METAL", "PAPER", "PLASTIC", "NON_RECYCLE", "SHOES")
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1
        val maxLabel = labels.getOrNull(maxIndex) ?: "Unknown"
        val maxConfidence = result.getOrNull(maxIndex)?.times(100)?.toInt() ?: 0

        binding.resultPercentage.text = "$maxConfidence%"
        binding.resultRecycle.text = maxLabel
        binding.textDescription.text = "Description of $maxLabel" // Adjust this line based on your use case

        binding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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
