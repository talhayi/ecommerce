package com.example.ecommerce.presentation.cart

import com.example.ecommerce.data.model.Product

data class CartState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
)