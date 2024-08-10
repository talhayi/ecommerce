package com.example.ecommerce.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.data.remote.retrofit.ProductApi
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.presentation.filter.FilterOptions
import com.example.ecommerce.presentation.filter.SortOption

class ProductPaging(
    private val filterOptions: FilterOptions,
    private val productApi: ProductApi
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        return try {
            val allProducts = productApi.productList(
                name = filterOptions.name,
                brands = filterOptions.brands,
                models = filterOptions.models
            )
            
            val sortedProducts = when (filterOptions.sortBy) {
                SortOption.OLD_TO_NEW -> allProducts.sortedBy { it.createdAt }
                SortOption.NEW_TO_OLD -> allProducts.sortedByDescending { it.createdAt }
                SortOption.PRICE_HIGH_TO_LOW -> allProducts.sortedByDescending { it.price?.toDouble() }
                SortOption.PRICE_LOW_TO_HIGH -> allProducts.sortedBy { it.price?.toDouble() }
                else -> allProducts
            }

            val pageSize = 4
            val startIndex = (page - 1) * pageSize
            val endIndex = (page * pageSize).coerceAtMost(sortedProducts.size)
            val products = sortedProducts.subList(startIndex, endIndex)

            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (endIndex == sortedProducts.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
