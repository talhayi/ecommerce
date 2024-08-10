package com.example.ecommerce.domain.usecase.products

import androidx.paging.PagingData
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.presentation.filter.FilterOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun executeGetProducts(filterOptions: FilterOptions): Flow<PagingData<Product>> {
        return repository.getProductList(filterOptions)
    }
}