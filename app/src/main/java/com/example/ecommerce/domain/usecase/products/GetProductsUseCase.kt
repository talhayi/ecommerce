package com.example.ecommerce.domain.usecase.products

import androidx.paging.PagingData
import com.example.ecommerce.domain.model.Product
import com.example.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun executeGetProducts(name: String? = null): Flow<PagingData<Product>> {
        return repository.getProductList(name ?: "")
    }
}