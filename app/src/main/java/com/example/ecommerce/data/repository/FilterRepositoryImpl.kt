package com.example.ecommerce.data.repository

import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.remote.retrofit.ProductApi
import com.example.ecommerce.domain.repository.FilterRepository
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : FilterRepository {
    override suspend fun getFilterList(
        brand: String?,
        model: String?
    ): List<Product> {
        return productApi.productList(brand = brand, model = model)
    }
}