package com.example.ecommerce.data.repository

import com.example.ecommerce.data.remote.dto.ProductDto
import com.example.ecommerce.data.remote.retrofit.ProductApi
import com.example.ecommerce.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {
    override suspend fun getProductList(): List<ProductDto> {
        return productApi.productList()
    }
}