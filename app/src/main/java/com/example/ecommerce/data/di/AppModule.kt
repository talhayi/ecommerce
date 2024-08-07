package com.example.ecommerce.data.di

import com.example.ecommerce.data.remote.retrofit.ApiUtils
import com.example.ecommerce.data.remote.retrofit.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductApi(): ProductApi {
        return ApiUtils.getProductApi()
    }
}