package com.example.ecommerce.presentation.products

import com.example.ecommerce.data.model.Product

data class ProductState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
)