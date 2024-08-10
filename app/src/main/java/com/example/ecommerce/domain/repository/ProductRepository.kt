package com.example.ecommerce.domain.repository

import androidx.paging.PagingData
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.presentation.filter.FilterOptions
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList(filterOptions: FilterOptions): Flow<PagingData<Product>>
}