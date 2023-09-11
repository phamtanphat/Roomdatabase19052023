package com.example.roomdatabase19052023.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase: RoomDatabase(){

}