package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.model.Product

interface FilterRepository {
    suspend fun getFilterList(): List<Product>
}