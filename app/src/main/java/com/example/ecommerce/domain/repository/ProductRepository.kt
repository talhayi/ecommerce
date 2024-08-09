package com.example.ecommerce.domain.repository

import androidx.paging.PagingData
import com.example.ecommerce.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList(name: String): Flow<PagingData<Product>>
}