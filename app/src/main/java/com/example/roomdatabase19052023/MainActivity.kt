package com.example.roomdatabase19052023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roomdatabase19052023.data.repository.ProductRepository
import com.example.roomdatabase19052023.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productRepository = ProductRepository(this)
        mainViewModel = ViewModelProvider(this@MainActivity, object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(productRepository) as T
            }
        })[MainViewModel::class.java]

        mainViewModel.listProductsLiveData().observe(this) {
            Log.d("BBB", it.size.toString())
        }

        mainViewModel.getListProducts()
    }
}