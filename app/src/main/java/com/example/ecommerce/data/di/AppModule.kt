package com.example.ecommerce.data.di

import android.content.Context
import androidx.room.Room
import com.example.ecommerce.data.local.ProductDao
import com.example.ecommerce.data.local.ProductDatabase
import com.example.ecommerce.data.remote.retrofit.ApiUtils
import com.example.ecommerce.data.remote.retrofit.ProductApi
import com.example.ecommerce.data.repository.CartRepositoryImpl
import com.example.ecommerce.data.repository.ProductRepositoryImpl
import com.example.ecommerce.domain.repository.CartRepository
import com.example.ecommerce.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideProductRepository(productApi: ProductApi): ProductRepository {
        return ProductRepositoryImpl(productApi)
    }

    @Provides
    @Singleton
    fun provideProductApi(): ProductApi {
        return ApiUtils.getProductApi()
    }

    @Provides
    @Singleton
    fun provideCartRepository(productDao: ProductDao): CartRepository {
        return CartRepositoryImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideProductDao(@ApplicationContext context: Context): ProductDao{
        val db = Room.databaseBuilder(context, ProductDatabase::class.java,"product.sqlite")
            .fallbackToDestructiveMigration().build()
        return db.getProductDao()
    }
}