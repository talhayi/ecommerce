package com.example.ecommerce.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ecommerce.data.remote.ProductPaging
import com.example.ecommerce.data.remote.retrofit.ProductApi
import com.example.ecommerce.domain.model.Product
import com.example.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {
    override suspend fun getProductList(name: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductPaging(name, productApi) }
        ).flow
    }
}