package com.example.ecommerce.data.remote.retrofit

import com.example.ecommerce.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ApiUtils {

    companion object {
        private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        fun getProductApi(): ProductApi {
            return RetrofitClient.getRetrofitClient(BASE_URL)
                .create(ProductApi::class.java)
        }
    }
}