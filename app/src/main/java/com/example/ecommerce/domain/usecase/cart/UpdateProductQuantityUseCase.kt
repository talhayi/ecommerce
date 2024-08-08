package com.example.ecommerce.domain.usecase.cart

import android.util.Log
import com.example.ecommerce.domain.repository.CartRepository
import com.example.ecommerce.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateProductQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: Int, quantity: Int): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            cartRepository.updateProductQuantity(productId, quantity)
            emit(Result.Success(Unit))
            Log.e("UpdateProductQuantityUseCase", "$productId, $quantity")
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}