package com.example.ecommerce.domain.usecase.cart

import com.example.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Flow<com.example.ecommerce.util.Result<Unit>> = flow {
        try {
            emit(com.example.ecommerce.util.Result.Loading)
            cartRepository.clearAllProducts()
            emit(com.example.ecommerce.util.Result.Success(Unit))
        } catch (e: Exception) {
            emit(com.example.ecommerce.util.Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}