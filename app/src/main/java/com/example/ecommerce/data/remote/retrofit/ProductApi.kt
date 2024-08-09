package com.example.ecommerce.data.remote.retrofit

import com.example.ecommerce.domain.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun productList(@Query("name") name: String? = null): List<Product>
}