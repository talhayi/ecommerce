package com.example.ecommerce.domain.usecase.products


import android.util.Log
import com.example.ecommerce.domain.model.Product
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    fun executeGetProducts(): Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.Loading)
            val productList = repository.getProductList()
            emit(Result.Success(productList))
            Log.e("executeGetProducts", "${productList[0].name}")
        } catch (e: HttpException) {
            emit(Result.Error(message = e.localizedMessage ?: "Error!"))
        } catch (e: IOException) {
            emit(Result.Error(message = "Could not reach internet"))
        }
    }
}