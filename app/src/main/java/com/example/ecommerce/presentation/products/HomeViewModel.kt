package com.example.ecommerce.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.usecase.products.GetProductsUseCase
import com.example.ecommerce.presentation.filter.FilterOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.ecommerce.util.Result
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetProductsUseCase
) : ViewModel() {

    private val _products = MutableStateFlow(ProductState())
    val products: StateFlow<ProductState> = _products

    fun setFilterOptions(options: FilterOptions) {
        getProducts(options)
    }

    fun getProducts(filterOptions: FilterOptions? = null) {
        viewModelScope.launch {
            useCase.executeGetProducts(filterOptions ?: FilterOptions())
                .onStart { _products.value = _products.value.copy(isLoading = true) }
                .collect { result ->
                    _products.value = when (result) {
                        is Result.Loading -> _products.value.copy(isLoading = true)
                        is Result.Success -> _products.value.copy(
                            isLoading = false,
                            products = result.data
                        )
                        is Result.Error -> _products.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
        }
    }
}