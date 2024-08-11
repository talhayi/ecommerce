package com.example.ecommerce.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.data.remote.retrofit.ProductApi
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.presentation.filter.FilterOptions
import com.example.ecommerce.presentation.filter.SortOption
import java.io.IOException

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
            )

            val filteredProducts = allProducts.filter { product ->
                (filterOptions.brands.isEmpty() || product.brand in filterOptions.brands) &&
                        (filterOptions.models.isEmpty() || product.model in filterOptions.models)
            }

            val sortedProducts = when (filterOptions.sortBy) {
                SortOption.OLD_TO_NEW -> allProducts.sortedBy { it.createdAt }
                SortOption.NEW_TO_OLD -> allProducts.sortedByDescending { it.createdAt }
                SortOption.PRICE_HIGH_TO_LOW -> allProducts.sortedByDescending { it.price?.toDouble() }
                SortOption.PRICE_LOW_TO_HIGH -> allProducts.sortedBy { it.price?.toDouble() }
                else -> filteredProducts
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
        } catch (e: IOException) {
            LoadResult.Error(Exception("Network error occurred. Please check your internet connection and try again."))
        } catch (e: IllegalArgumentException) {
            LoadResult.Error(Exception("Invalid input provided: ${e.message}."))
        } catch (e: Exception) {
            LoadResult.Error(Exception("An unexpected error occurred: ${e.message}. Please try again later."))
        }
    }
}
