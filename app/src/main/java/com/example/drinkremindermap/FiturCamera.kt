package com.example.drinkremindermap

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

class FiturCamera : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var cameraButton: Button
    private lateinit var currentPhotoPath: String

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            // Handle permission denied
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Handle successful image capture
            processImage()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fitur_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.capturedImageView)
        resultTextView = view.findViewById(R.id.resultTextView)
        cameraButton = view.findViewById(R.id.cameraButton)

        cameraButton.setOnClickListener {
            checkCameraPermissionAndOpen()
        }
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Show an explanation to the user
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: Exception) {
            null
        }

        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.drinkremindermap.fileprovider",
                it
            )
            cameraLauncher.launch(photoURI)
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun processImage() {
        // Implement your image processing logic here
        // For example:
        // val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        // detectWaterInBottle(bitmap)
    }

    private fun detectWaterInBottle(bitmap: android.graphics.Bitmap) {
        // Implement your water detection logic here
    }
}