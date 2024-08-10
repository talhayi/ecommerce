package com.example.ecommerce.presentation.products

import androidx.paging.PagingData
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.presentation.filter.FilterOptions

data class ProductState(
    val isLoading: Boolean = false,
    val products: PagingData<Product> = PagingData.empty(),
    val error: String? = null,
    val filterOptions: FilterOptions? = null
)