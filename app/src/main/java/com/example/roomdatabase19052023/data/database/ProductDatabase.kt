package com.example.roomdatabase19052023.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.roomdatabase19052023.data.database.converter.BitmapConverter

@Database(entities = [ProductEntity::class], version = 1)
@TypeConverters(BitmapConverter::class)
abstract class ProductDatabase: RoomDatabase(){
    abstract fun productDAO(): ProductDAO

    companion object {
        var instance: ProductDatabase? = null
        fun getDatabase(context: Context): ProductDatabase {
            var tmpInstance = instance
            if (tmpInstance == null) {
                tmpInstance = Room.databaseBuilder(
                    context = context,
                    klass = ProductDatabase::class.java,
                    name = "product-database"
                ).build()
            }
            return tmpInstance
        }
    }
}