package com.example.ecommerce.data.remote.retrofit

import com.example.ecommerce.data.model.Product
import com.example.ecommerce.presentation.filter.SortOption
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun productList(
        @Query("name") name: String? = null,
        @Query("brand") brand: String? = null,
        @Query("model") model: String? = null,
    ): List<Product>
}