package com.example.ecommerce.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.data.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(product: Product): Long

    @Query("SELECT * FROM products")
    suspend fun getProductList(): List<Product>

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("UPDATE products SET quantity = :quantity WHERE id = :productId")
    suspend fun updateProductQuantity(productId: Int, quantity: Int)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}