package com.example.roomdatabase19052023.data.repository

import android.content.Context
import com.example.roomdatabase19052023.data.database.ProductDatabase
import com.example.roomdatabase19052023.data.database.ProductEntity

class ProductRepository(context: Context) {
    private var productDAO = ProductDatabase.getDatabase(context).productDAO()

    suspend fun getListProducts(): List<ProductEntity> {
        return productDAO.queryProducts()
    }
}