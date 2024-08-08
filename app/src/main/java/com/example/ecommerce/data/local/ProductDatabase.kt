package com.example.ecommerce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.domain.model.Product

@Database(entities = [Product::class], version = 3, exportSchema = false)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun getProductDao(): ProductDao
}