package com.example.ecommerce.data.remote.retrofit

import com.example.ecommerce.data.remote.dto.ProductDto
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun productList(): List<ProductDto>
}