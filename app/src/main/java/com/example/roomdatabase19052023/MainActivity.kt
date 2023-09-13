package com.example.roomdatabase19052023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.roomdatabase19052023.data.database.ProductDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            val productDatabase = ProductDatabase.getDatabase(this@MainActivity)
            val productDAO = productDatabase?.productDAO()
            try {
                val listProduct = async { productDAO?.getProduct() }.await()
                Log.d("BBB", listProduct?.size.toString())
            } catch(e: Exception) {

            }
        }
    }
}