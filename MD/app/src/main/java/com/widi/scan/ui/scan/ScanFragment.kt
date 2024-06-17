package com.widi.scan.ui.scan

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.TAG
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.widi.scan.R
import com.widi.scan.databinding.BottomSheetDialogBinding
import com.widi.scan.databinding.FragmentScanBinding
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
            Manifest.permission.ACCESS_FINE_LOCATION
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
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        wasteModel = WasteClassification(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScanBinding.bind(view)

        val scanAnimator = ObjectAnimator.ofFloat(binding.scanLine, "translationY", 0f, 480f)
        scanAnimator.duration = 1500
        scanAnimator.repeatCount = ObjectAnimator.INFINITE
        scanAnimator.repeatMode = ObjectAnimator.REVERSE

        binding.apply {
            buttonGallery.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            buttonResult.setOnClickListener {
                if (currentImageUri == null) {
                    showToast(getString(R.string.empty_image_warning))
                    return@setOnClickListener
                }
                scanAnimator.start()
                Handler(Looper.getMainLooper()).postDelayed({
                    scanAnimator.cancel()
                    classifyImage()
                }, 3000)
            }

            btnBack.setOnClickListener {
                findNavController().safeNavigate(ScanFragmentDirections.actionScanFragmentToHomeFragment())
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("imageUri")?.observe(
            viewLifecycleOwner
        ) { imageUri ->
            currentImageUri = Uri.parse(imageUri)
            showImage()
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
        findNavController().safeNavigate(ScanFragmentDirections.actionScanFragmentToCameraFragment())
    }

    private fun classifyImage() {
        currentImageUri?.let { uri ->
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val preprocessedImage = wasteModel.preprocessImage(bitmap)
            val result = wasteModel.classify(preprocessedImage)

            val labels = resources.getStringArray(R.array.waste_labels)
            val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1
            val maxLabel = labels.getOrNull(maxIndex) ?: getString(R.string.no_data)
            val maxConfidence = result.getOrNull(maxIndex)?.times(100)?.toInt() ?: 0
            val timestamp = System.currentTimeMillis()

            saveClassificationToDatabase(uri.toString(), maxLabel, timestamp, maxConfidence)
            showBottomSheetDialog(result)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun saveClassificationToDatabase(imageUri: String, label: String, timestamp: Long, confidence: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val historyData = hashMapOf(
                "userId" to user.uid,
                "imageUri" to imageUri,
                "label" to label,
                "timestamp" to timestamp,
                "confidence" to confidence
            )

            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("histories")
                .add(historyData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        } else {
            Log.e(TAG, "User is not authenticated")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showBottomSheetDialog(result: FloatArray) {
        val binding = BottomSheetDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)

        val labels = resources.getStringArray(R.array.waste_labels)
        val descriptions = resources.getStringArray(R.array.waste_descriptions)
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1
        val maxLabel = labels.getOrNull(maxIndex) ?: getString(R.string.no_data)
        val maxConfidence = result.getOrNull(maxIndex)?.times(100)?.toInt() ?: 0

        binding.resultPercentage.text = "$maxConfidence%"
        binding.resultRecycle.text = maxLabel
        binding.textDescription.text = descriptions.getOrNull(maxIndex) ?: getString(R.string.lorem_ipsum)

        binding.btnRecommendation.setOnClickListener {
            findNavController().safeNavigate(ScanFragmentDirections.actionScanFragmentToMapsFragment())
            dialog.dismiss()
        }

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
}
