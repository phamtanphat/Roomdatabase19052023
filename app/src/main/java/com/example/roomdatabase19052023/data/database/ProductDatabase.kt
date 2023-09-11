package com.example.roomdatabase19052023.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase: RoomDatabase(){
    abstract fun productDAO(): ProductDAO

    fun getDatabase(context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ProductDatabase::class.java,
            name = "product-database"
        ).build()
    }
}