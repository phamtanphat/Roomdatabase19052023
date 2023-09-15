package com.example.roomdatabase19052023

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roomdatabase19052023.common.AppResource
import com.example.roomdatabase19052023.data.database.ProductEntity
import com.example.roomdatabase19052023.data.repository.ProductRepository
import com.example.roomdatabase19052023.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productRepository = ProductRepository(this)
        mainViewModel = ViewModelProvider(this@MainActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(productRepository) as T
            }
        })[MainViewModel::class.java]

        mainViewModel.listProductsLiveData().observe(this) {
            when (it) {
                is AppResource.Success -> Log.d("BBB", it.data?.size.toString())
                is AppResource.Error -> Log.d("BBB", it.message.toString())
            }
        }

        mainViewModel.insertLiveData().observe(this) {
            when (it) {
                is AppResource.Success -> mainViewModel.getListProducts()
                is AppResource.Error -> Log.d("BBB", it.message.toString())
            }
        }

        mainViewModel.getListProducts()
        mainViewModel.insertProduct(
            ProductEntity(
                id = null,
                name = "Data 2",
                description = "Description 2",
                price = 30.000,
                image = BitmapFactory.decodeResource(resources, R.drawable.no_image)
            )
        )
    }
}