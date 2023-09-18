package com.example.roomdatabase19052023

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roomdatabase19052023.common.AppResource
import com.example.roomdatabase19052023.data.database.ProductEntity
import com.example.roomdatabase19052023.data.repository.ProductRepository
import com.example.roomdatabase19052023.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private var layoutLoading: RelativeLayout? = null
    private var edtName: EditText? = null
    private var edtPrice: EditText? = null
    private var edtDescription: EditText? = null
    private var img: ImageView? = null
    private var btnSave: Button? = null
    private var btnCancel: Button? = null

    private var REQUEST_CODE_CAMERA = 123
    private var bitmapImage: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productRepository = ProductRepository(this)
        mainViewModel = ViewModelProvider(this@MainActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(productRepository) as T
            }
        })[MainViewModel::class.java]

        layoutLoading = findViewById(R.id.relative_layout_loading)
        edtName = findViewById(R.id.edit_text_name)
        edtPrice = findViewById(R.id.edit_text_price)
        edtDescription = findViewById(R.id.edit_text_description)
        img = findViewById(R.id.image_view_product)
        btnSave = findViewById(R.id.button_save)
        btnCancel = findViewById(R.id.button_cancel)

        mainViewModel.loadingLiveData().observe(this) {
            layoutLoading?.isGone = it
        }
        mainViewModel.insertLiveData().observe(this) {
            when (it) {
                is AppResource.Success -> {
                    Toast.makeText(this@MainActivity, "Insert successfully!!", Toast.LENGTH_SHORT).show()
                }

                is AppResource.Error -> {
                    Toast.makeText(this@MainActivity, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        img?.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.CAMERA),
                        REQUEST_CODE_CAMERA
                    )
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultActivityCamera.launch(intent)
            }
        }

        btnSave?.setOnClickListener {
            val name = edtName?.editableText.toString()
            val price = edtPrice?.editableText.toString()
            val description = edtDescription?.editableText.toString()
            if (name.isBlank() || price.isBlank() || description.isBlank() || bitmapImage == null) return@setOnClickListener
            mainViewModel.insertProduct(
                ProductEntity(
                    id = null,
                    name = name,
                    description = description,
                    price = price.toDoubleOrNull() ?: 0.0,
                    image = bitmapImage
                )
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            val resultCamera = grantResults.getOrNull(0)
            if (resultCamera == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultActivityCamera.launch(intent)
            }
        }
    }

    private val resultActivityCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                val bitmap = it.data?.extras?.get("data") as Bitmap
                bitmapImage = bitmap
                img?.setImageBitmap(bitmap)
            }
        }
}