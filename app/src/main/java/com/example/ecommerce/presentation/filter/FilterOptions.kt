package com.example.ecommerce.presentation.filter

import java.io.Serializable

data class FilterOptions(
    val name: String? = null,
    val brands: List<String> = emptyList(),
    val models: List<String> = emptyList(),
    val sortBy: SortOption = SortOption.NONE
):Serializable

enum class SortOption {
    NONE,
    OLD_TO_NEW,
    NEW_TO_OLD,
    PRICE_HIGH_TO_LOW,
    PRICE_LOW_TO_HIGH
}