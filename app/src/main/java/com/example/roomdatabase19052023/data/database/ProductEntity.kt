package com.example.roomdatabase19052023.data.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity (
    @PrimaryKey val id: Int?,
    val name: String?,
    val description: String?,
    val price: Double?,
    val image: Bitmap?,
)