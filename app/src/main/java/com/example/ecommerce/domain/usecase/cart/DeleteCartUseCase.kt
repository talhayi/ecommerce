package com.example.ecommerce.domain.usecase.cart

import com.example.ecommerce.domain.model.Product
import com.example.ecommerce.domain.repository.CartRepository
import com.example.ecommerce.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(product: Product): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            cartRepository.deleteCart(product)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
