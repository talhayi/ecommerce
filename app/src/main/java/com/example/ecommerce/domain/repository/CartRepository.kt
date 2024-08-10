package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.model.Product

interface CartRepository {
    suspend fun saveCart(product: Product)
    suspend fun getCartList():List<Product>
    suspend fun deleteCart(product: Product)
    suspend fun updateProductQuantity(productId: Int, quantity: Int)
    suspend fun clearAllProducts()
}