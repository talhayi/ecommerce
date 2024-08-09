package com.example.ecommerce.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.data.remote.retrofit.ProductApi
import com.example.ecommerce.domain.model.Product

class ProductPaging(
    private val name: String,
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
            val allProducts = productApi.productList(name = name)
            val pageSize = 4
            val startIndex = (page - 1) * pageSize
            val endIndex = (page * pageSize).coerceAtMost(allProducts.size)
            val products = allProducts.subList(startIndex, endIndex)

            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (endIndex == allProducts.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}