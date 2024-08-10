package com.example.ecommerce.domain.usecase.cart

import android.util.Log
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.repository.CartRepository
import com.example.ecommerce.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCartsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
     suspend operator fun invoke(): Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.Loading)
            val cartList = cartRepository.getCartList()
            emit(Result.Success(cartList))
            Log.e("GetCartsUseCase", "$cartList")
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}