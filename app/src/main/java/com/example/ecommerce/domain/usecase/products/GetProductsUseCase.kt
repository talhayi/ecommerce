package com.example.ecommerce.domain.usecase.products

import androidx.paging.PagingData
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.presentation.filter.FilterOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.ecommerce.util.Result

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun executeGetProducts(filterOptions: FilterOptions): Flow< Result<PagingData<Product>>> = flow {
        try {
            emit(Result.Loading)
            repository.getProductList(filterOptions)
                .collect { pagingData ->
                    emit(Result.Success(pagingData))
                }
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}