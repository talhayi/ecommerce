package com.example.ecommerce.domain.usecase.filter

import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.repository.FilterRepository
import com.example.ecommerce.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {
    suspend operator fun invoke(
        brand: String? = null,
        model: String? = null
    ): Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.Loading)
            val filterList = filterRepository.getFilterList(brand = brand, model = model)
            emit(Result.Success(filterList))
        } catch (e: HttpException) {
            if (e.code() == 404) {
                emit(Result.Error("No Products Available."))
            } else {
                emit(Result.Error("Network error occurred. Please check your internet connection and try again."))
            }
        } catch (e: IOException) {
            emit(Result.Error("Network error occurred. Please check your internet connection and try again."))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}