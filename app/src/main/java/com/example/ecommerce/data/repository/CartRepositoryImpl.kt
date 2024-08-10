package com.example.ecommerce.data.repository

import com.example.ecommerce.data.local.ProductDao
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
): CartRepository {
    override suspend fun saveCart(product: Product) {
        productDao.saveProduct(product)
    }

    override suspend fun getCartList(): List<Product> =
        productDao.getProductList()

    override suspend fun deleteCart(product: Product) {
       productDao.deleteProduct(product)
    }

    override suspend fun updateProductQuantity(productId: Int, quantity: Int) {
        productDao.updateProductQuantity(productId, quantity)
    }

    override suspend fun clearAllProducts() {
        productDao.deleteAllProducts()
    }
}