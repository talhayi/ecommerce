package com.example.ecommerce.data.remote.retrofit

import com.example.ecommerce.domain.model.Product
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun productList(): List<Product>
}