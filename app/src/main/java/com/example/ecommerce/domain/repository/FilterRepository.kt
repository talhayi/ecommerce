package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.model.Product

interface FilterRepository {
    suspend fun getFilterList(
        brand: String? = null,
        model: String? = null,
    ): List<Product>
}