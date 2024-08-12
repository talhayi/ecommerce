package com.example.ecommerce.domain.usecase.filter

import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.repository.FilterRepository
import com.example.ecommerce.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.Loading)
            val filterList = filterRepository.getFilterList()
            if (filterList.isEmpty()) {
                emit(Result.Error("Filter is empty"))
            } else {
                emit(Result.Success(filterList))
            }
        } catch (e: IOException) {
            emit(Result.Error("Network error occurred. Please check your internet connection and try again."))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}