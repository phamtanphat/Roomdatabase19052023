package com.example.roomdatabase19052023.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProductDAO {

    // Get data from database
    @Query("SELECT * FROM product")
    suspend fun queryProducts(): List<ProductEntity>
}